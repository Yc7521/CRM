package com.crm.controller;

import com.crm.dao.DataSet;
import com.crm.model.Client;
import com.crm.model.Cost;
import com.crm.model.Employee;
import com.crm.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.crm.conf.Data.maxSize;

@Controller
@RequestMapping("cost")
public class CostController {
    private final DataSet dataSet;

    public CostController(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @GetMapping({"", "/index"})
    public String index(@RequestParam(defaultValue = "0") int page, Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        final Page<Cost> costPage = dataSet.costs.findAll(id);
        model.addAttribute("model", costPage);
        return "cost/index";
    }

    /**
     * @param search by employee name
     * @param page
     * @param model
     * @return
     */
    @GetMapping("search")
    public String search(@RequestParam(required = false) String search,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        if (search.isEmpty()) return "redirect:/cost";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.costs.findAllByEmployee_Name(id, search));
        return "cost/index";
    }

    /**
     * @param search by employee id
     * @param page
     * @param model
     * @return
     */
    @GetMapping("searchEmployeeId")
    public String searchEmployeeId(@RequestParam(required = false) Integer search,
                                   @RequestParam(defaultValue = "0") int page,
                                   Model model) {
        if (search == null) return "redirect:/cost";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.costs.findAllByEmployee_Id(id, search));
        return "cost/index";
    }

    // TODO: maybe need a method to search by product or client

    @GetMapping("create")
    public String create(@RequestParam(required = false) Integer id, Model model) {
        final Cost cost = new Cost();
        // get a user who is logged in
        final Object principal = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (principal instanceof Client client) {
            cost.setClient(client);
        } else if (principal instanceof Employee employee) {
            cost.setEmployee(employee);
        }

        if (id != null) {
            final Optional<Product> product = dataSet.products.findById(id);
            if (product.isPresent()) {
                cost.setProduct(product.get());
            } else {
                return "redirect:/cost/create";
            }
        }
        model.addAttribute("model", cost);
        model.addAttribute("clients", dataSet.clients.findAll());
        model.addAttribute("products", dataSet.products.findAll());
        model.addAttribute("employees", dataSet.employees.findAll());
        return "cost/create";
    }

    @PostMapping("create")
    public String create(Cost cost,
                         @RequestParam Integer client_id,
                         @RequestParam Integer product_id,
                         @RequestParam Integer employee_id) {
        final Optional<Client> client = dataSet.clients.findById(client_id);
        final Optional<Product> product = dataSet.products.findById(product_id);
        final Optional<Employee> employee = dataSet.employees.findById(employee_id);
        if (client.isPresent() && product.isPresent() && employee.isPresent()) {
            cost.setClient(client.get());
            cost.setProduct(product.get());
            cost.setEmployee(employee.get());
            dataSet.costs.save(cost);
            return "redirect:/cost/";
        }
        return "redirect:/notfound";
    }

    @GetMapping("details")
    public String details(@RequestParam(required = false) Integer id, Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Cost> cost = dataSet.costs.findById(id);
        if (cost.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", cost.get());
        return "cost/details";
    }

    @GetMapping("edit")
    public String edit(@RequestParam(required = false) Integer id, Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Cost> cost = dataSet.costs.findById(id);
        if (cost.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", cost.get());
        model.addAttribute("clients", dataSet.clients.findAll());
        model.addAttribute("products", dataSet.products.findAll());
        model.addAttribute("employees", dataSet.employees.findAll());
        return "cost/edit";
    }

    @PostMapping("edit")
    public String edit(Cost cost,
                       @RequestParam Integer client_id,
                       @RequestParam Integer product_id,
                       @RequestParam Integer employee_id,
                       Model model) {
        final Optional<Cost> data = dataSet.costs.findById(cost.getId());
        final Optional<Client> client = dataSet.clients.findById(client_id);
        final Optional<Product> product = dataSet.products.findById(product_id);
        final Optional<Employee> employee = dataSet.employees.findById(employee_id);
        if (data.isPresent() && client.isPresent() && product.isPresent() && employee.isPresent()) {
            cost.setClient(client.get());
            cost.setProduct(product.get());
            cost.setEmployee(employee.get());
            dataSet.costs.save(cost);
            model.addAttribute("model", cost);
            return "redirect:/cost";
        }
        return "redirect:/notfound";
    }

    @GetMapping("delete")
    public String delete(@RequestParam(required = false) Integer id, Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Cost> cost = dataSet.costs.findById(id);
        if (cost.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", cost.get());
        return "cost/delete";
    }

    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam int id) {
        final Optional<Cost> cost = dataSet.costs.findById(id);
        if (cost.isEmpty()) return "redirect:/notfound";

        dataSet.costs.delete(cost.get());
        return "redirect:/cost";
    }
}