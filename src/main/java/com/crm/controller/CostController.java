package com.crm.controller;

import com.crm.dao.DataSet;
import com.crm.model.Client;
import com.crm.model.Cost;
import com.crm.model.Employee;
import com.crm.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static com.crm.conf.Data.maxSize;

/**
 * A controller for the cost page.
 */
@Controller
@RequestMapping("cost")
public class CostController {
    private final DataSet dataSet;

    /**
     * Constructor
     *
     * @param dataSet a {@link DataSet}
     */
    public CostController(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * Get {@link Cost} by id
     *
     * @param id      the id of {@link Cost}
     * @param dataSet data set
     * @throws HttpClientErrorException if {@link Cost} not found
     */
    static Cost getCost(Integer id, DataSet dataSet) {
        if (id == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Cost not found");

        Optional<Cost> cost = dataSet.costs.findById(id);
        if (cost.isEmpty())
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Cost not found");

        return cost.get();
    }

    /**
     * [GET]
     * mapping to /cost/[index]?page=0
     *
     * @param page a number of page
     */
    @GetMapping({"", "index"})
    public String index(@RequestParam(defaultValue = "0") int page, Model model) {
        PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        Page<Cost> costPage = this.dataSet.costs.findAll(id);
        model.addAttribute("model", costPage);
        return "cost/index";
    }

    /**
     * [GET]
     * mapping to /cost/search?page=0&search=
     *
     * @param search the name of {@link Employee}
     * @param page   a number of page
     */
    @GetMapping("search")
    public String search(@RequestParam(required = false) String search,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        if (search.isEmpty()) return "redirect:/cost";
        PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", this.dataSet.costs.findAllByEmployee_Name(id, search));
        return "cost/index";
    }

    // TODO: maybe need a method to search by product or client

    /**
     * [GET]
     * mapping to /cost/searchEmployeeId?page=0&search=
     *
     * @param search the id of {@link Employee}
     * @param page   a number of page
     */
    @GetMapping("searchEmployeeId")
    public String searchEmployeeId(@RequestParam(required = false) Integer search,
                                   @RequestParam(defaultValue = "0") int page,
                                   Model model) {
        if (search == null) return "redirect:/cost";
        PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", this.dataSet.costs.findAllByEmployee_Id(id, search));
        return "cost/index";
    }

    /**
     * [GET]
     * mapping to /cost/create?id=0
     *
     * @param id the id of {@link Product}
     */
    @GetMapping("create")
    public void create(@RequestParam(required = false) Integer id, Model model) {
        Cost cost = new Cost();
        // get a user who is logged in
        Object principal = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (principal instanceof Client client) {
            cost.setClient(client);
        } else if (principal instanceof Employee employee) {
            cost.setEmployee(employee);
        }

        if (id != null) cost.setProduct(ProductController.getProduct(id, this.dataSet));

        model.addAttribute("model", cost);
        model.addAttribute("clients", this.dataSet.clients.findAll());
        model.addAttribute("products", this.dataSet.products.findAll());
        model.addAttribute("employees", this.dataSet.employees.findAll());
    }

    /**
     * [POST]
     * mapping to /cost/create
     *
     * @param cost        the {@link Cost}
     * @param client_id   the id of {@link Client}
     * @param product_id  the id of {@link Product}
     * @param employee_id the id of {@link Employee}
     */
    @PostMapping("create")
    public String create(Cost cost,
                         @RequestParam Integer client_id,
                         @RequestParam Integer product_id,
                         @RequestParam Integer employee_id) {
        cost.setClient(ClientController.getClient(client_id, this.dataSet));
        cost.setProduct(ProductController.getProduct(product_id, this.dataSet));
        cost.setEmployee(EmployeeController.getEmployee(employee_id, this.dataSet));
        this.dataSet.costs.save(cost);
        return "redirect:/cost";
    }

    /**
     * [GET]
     * mapping to /cost/details?id=0
     *
     * @param id the id of {@link Cost}
     */
    @GetMapping("details")
    public void details(@RequestParam(required = false) Integer id, Model model) {
        Cost cost1 = CostController.getCost(id, this.dataSet);
        model.addAttribute("model", cost1);
    }

    /**
     * [GET]
     * mapping to /cost/edit?id=0
     *
     * @param id the id of {@link Cost}
     */
    @GetMapping("edit")
    public void edit(@RequestParam(required = false) Integer id, Model model) {
        Cost cost = CostController.getCost(id, this.dataSet);
        model.addAttribute("model", cost);
        model.addAttribute("clients", this.dataSet.clients.findAll());
        model.addAttribute("products", this.dataSet.products.findAll());
        model.addAttribute("employees", this.dataSet.employees.findAll());
    }

    /**
     * [POST]
     * mapping to /cost/edit
     *
     * @param cost        the {@link Cost}
     * @param client_id   the id of {@link Client}
     * @param product_id  the id of {@link Product}
     * @param employee_id the id of {@link Employee}
     */
    @PostMapping("edit")
    public String edit(Cost cost,
                       @RequestParam Integer client_id,
                       @RequestParam Integer product_id,
                       @RequestParam Integer employee_id,
                       Model model) {
        // check if exists
        final Cost cost1 = CostController.getCost(cost.getId(), this.dataSet);
        cost1.setTime(cost.getTime());
        cost1.setClient(ClientController.getClient(client_id, this.dataSet));
        cost1.setProduct(ProductController.getProduct(product_id, this.dataSet));
        cost1.setEmployee(EmployeeController.getEmployee(employee_id, this.dataSet));
        this.dataSet.costs.save(cost1);
        return "redirect:/cost";
    }

    /**
     * [GET]
     * mapping to /cost/delete?id=0
     *
     * @param id the id of {@link Cost}
     */
    @GetMapping("delete")
    public void delete(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", CostController.getCost(id, this.dataSet));
    }

    /**
     * [POST]
     * mapping to /cost/delete
     *
     * @param id the id of {@link Cost}
     */
    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam int id) {
        this.dataSet.costs.delete(CostController.getCost(id, this.dataSet));
        return "redirect:/cost";
    }
}