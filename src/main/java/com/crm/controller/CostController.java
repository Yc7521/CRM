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
    public CostController(final DataSet dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * Get {@link Cost} by id
     *
     * @param id      the id of {@link Cost}
     * @param dataSet data set
     * @throws HttpClientErrorException if {@link Cost} not found
     */
    static Cost getCost(final Integer id, final DataSet dataSet) {
        if (id == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Cost not found");

        final Optional<Cost> cost = dataSet.costs.findById(id);
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
    public String index(@RequestParam(defaultValue = "0") final int page,
                        final Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        final Page<Cost> costPage = dataSet.costs.findAll(id);
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
    public String search(@RequestParam(required = false) final String search,
                         @RequestParam(defaultValue = "0") final int page,
                         final Model model) {
        if (search.isEmpty()) return "redirect:/cost";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.costs.findAllByEmployee_Name(id, search));
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
    public String searchEmployeeId(@RequestParam(required = false) final Integer search,
                                   @RequestParam(defaultValue = "0") final int page,
                                   final Model model) {
        if (search == null) return "redirect:/cost";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.costs.findAllByEmployee_Id(id, search));
        return "cost/index";
    }

    /**
     * [GET]
     * mapping to /cost/searchUnhandled?page=0
     *
     * @param page a number of page
     */
    @GetMapping("searchUnhandled")
    public String searchUnhandled(@RequestParam(defaultValue = "0") final int page,
                                  final Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.costs.findAllByEmployee_Name(id, "Anonymous"));
        return "cost/index";
    }

    /**
     * [GET]
     * mapping to /cost/handle?id=0
     *
     * @param id the id of {@link Cost}
     */
    @GetMapping("handle")
    public void handle(@RequestParam(required = false) final Integer id,
                       final Model model) throws Exception {
        final Cost cost = getCost(id, dataSet);
        final Object principal = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (principal instanceof Employee employee) {
            cost.setEmployee(employee);
        } else {
            throw new Exception("Employee not found");
        }

        model.addAttribute("model", cost);
    }

    /**
     * [POST]
     * mapping to /cost/handle?id=0
     *
     * @param id the id of {@link Cost}
     */
    @PostMapping("handle")
    public String handleConfirmed(@RequestParam(
            required = false) final Integer id) throws Exception {
        final Cost cost = getCost(id, dataSet);
        final Object principal = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (principal instanceof Employee employee) {
            cost.setEmployee(EmployeeController.getEmployee(employee.getId(), dataSet));
        } else {
            throw new Exception("Employee not found");
        }
        // dec num here when it is confirmed.
        final Product product = cost.getProduct();
        if (product.getNum() > 0) {
            product.decNum();
        } else {
            throw new Exception("Product num is not enough");
        }
        cost.setProduct(dataSet.products.save(product));
        dataSet.costs.save(cost);
        return "redirect:/cost";
    }


    /**
     * [GET]
     * mapping to /cost/create?id=0
     *
     * @param id the id of {@link Product}
     */
    @GetMapping("create")
    public void create(@RequestParam(required = false) final Integer id,
                       final Model model) {
        final Cost cost = new Cost();
        // get a user who is logged in
        final Object principal = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (principal instanceof Client client) {
            cost.setClient(client);
        } else if (principal instanceof Employee employee) {
            cost.setEmployee(employee);
        }

        if (id != null) cost.setProduct(ProductController.getProduct(id, dataSet));

        model.addAttribute("model", cost);
        model.addAttribute("clients", dataSet.clients.findAll());
        model.addAttribute("products", dataSet.products.findAll());
        model.addAttribute("employees", dataSet.employees.findAll());
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
    public String create(final Cost cost,
                         @RequestParam final Integer client_id,
                         @RequestParam final Integer product_id,
                         @RequestParam final Integer employee_id) {
        cost.setClient(ClientController.getClient(client_id, dataSet));
        cost.setProduct(ProductController.getProduct(product_id, dataSet));
        cost.setEmployee(EmployeeController.getEmployee(employee_id, dataSet));
        dataSet.costs.save(cost);
        return "redirect:/cost";
    }

    /**
     * [GET]
     * mapping to /cost/details?id=0
     *
     * @param id the id of {@link Cost}
     */
    @GetMapping("details")
    public void details(@RequestParam(required = false) final Integer id,
                        final Model model) {
        final Cost cost1 = getCost(id, dataSet);
        model.addAttribute("model", cost1);
    }

    /**
     * [GET]
     * mapping to /cost/edit?id=0
     *
     * @param id the id of {@link Cost}
     */
    @GetMapping("edit")
    public void edit(@RequestParam(required = false) final Integer id,
                     final Model model) {
        final Cost cost = getCost(id, dataSet);
        model.addAttribute("model", cost);
        model.addAttribute("clients", dataSet.clients.findAll());
        model.addAttribute("products", dataSet.products.findAll());
        model.addAttribute("employees", dataSet.employees.findAll());
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
    public String edit(final Cost cost,
                       @RequestParam final Integer client_id,
                       @RequestParam final Integer product_id,
                       @RequestParam final Integer employee_id,
                       final Model model) {
        // check if exists
        Cost cost1 = getCost(cost.getId(), dataSet);
        cost1.setTime(cost.getTime());
        cost1.setClient(ClientController.getClient(client_id, dataSet));
        cost1.setProduct(ProductController.getProduct(product_id, dataSet));
        cost1.setEmployee(EmployeeController.getEmployee(employee_id, dataSet));
        dataSet.costs.save(cost1);
        return "redirect:/cost";
    }

    /**
     * [GET]
     * mapping to /cost/delete?id=0
     *
     * @param id the id of {@link Cost}
     */
    @GetMapping("delete")
    public void delete(@RequestParam(required = false) final Integer id,
                       final Model model) {
        model.addAttribute("model", getCost(id, dataSet));
    }

    /**
     * [POST]
     * mapping to /cost/delete
     *
     * @param id the id of {@link Cost}
     */
    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam final int id) {
        dataSet.costs.delete(getCost(id, dataSet));
        return "redirect:/cost";
    }
}