package com.crm.controller;

import com.crm.dao.DataSet;
import com.crm.model.Client;
import com.crm.model.Employee;
import com.crm.model.Plan;
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
@RequestMapping("plan")
public class PlanController {
    private final DataSet dataSet;

    public PlanController(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @GetMapping({"", "/index"})
    public String index(@RequestParam(defaultValue = "0") int page, Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        final Page<Plan> planPage = dataSet.plans.findAll(id);
        model.addAttribute("model", planPage);
        return "plan/index";
    }

    @GetMapping("search")
    public String search(@RequestParam(required = false) String search,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        if (search.isEmpty()) return "redirect:/plan";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.plans.findAllByClient_Name(id, search));
        return "plan/index";
    }

    @GetMapping("searchClientId")
    public String searchClientId(@RequestParam(required = false) Integer search,
                                 @RequestParam(defaultValue = "0") int page,
                                 Model model) {
        if (search == null) return "redirect:/plan";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.plans.findAllByClient_Id(id, search));
        return "plan/index";
    }

    @GetMapping("searchEmployeeId")
    public String searchEmployeeId(@RequestParam(required = false) Integer search,
                                   @RequestParam(defaultValue = "0") int page,
                                   Model model) {
        if (search == null) return "redirect:/plan";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.plans.findAllByEmployee_Id(id, search));
        return "plan/index";
    }

    /**
     * @param employee_id employee id
     * @param client_id   client id
     * @param model       model
     * @return /plan/create if no error occurred
     */
    @GetMapping("create")
    public String create(@RequestParam(required = false) Integer employee_id,
                         @RequestParam(required = false) Integer client_id,
                         Model model) {
        final Plan plan = new Plan();
        if (employee_id != null) {
            final Optional<Employee> employee = dataSet.employees.findById(employee_id);
            if (employee.isPresent()) {
                plan.setEmployee(employee.get());
            } else {
                return "redirect:/plan/create";
            }
        }
        if (client_id != null) {
            final Optional<Client> client = dataSet.clients.findById(client_id);
            if (client.isPresent()) {
                plan.setClient(client.get());
            } else {
                return "redirect:/plan/create";
            }
        }
        model.addAttribute("model", plan);
        model.addAttribute("employees", dataSet.employees.findAll());
        model.addAttribute("clients", dataSet.clients.findAll());
        return "plan/create";
    }

    @PostMapping("create")
    public String create(Plan plan,
                         @RequestParam Integer employee_id,
                         @RequestParam Integer client_id) {
        final Optional<Employee> employee = dataSet.employees.findById(employee_id);
        final Optional<Client> client = dataSet.clients.findById(client_id);
        if (employee.isPresent() && client.isPresent()) {
            plan.setEmployee(employee.get());
            plan.setClient(client.get());
            dataSet.plans.save(plan);
            return "redirect:/plan/";
        }
        return "redirect:/notfound";
    }

    @GetMapping("details")
    public String details(@RequestParam(required = false) Integer id, Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Plan> plan = dataSet.plans.findById(id);
        if (plan.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", plan.get());
        return "plan/details";
    }

    @GetMapping("edit")
    public String edit(@RequestParam(required = false) Integer id, Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Plan> plan = dataSet.plans.findById(id);
        if (plan.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", plan.get());
        model.addAttribute("employees", dataSet.employees.findAll());
        model.addAttribute("clients", dataSet.clients.findAll());
        return "plan/edit";
    }

    @PostMapping("edit")
    public String edit(Plan plan,
                       @RequestParam Integer employee_id,
                       @RequestParam Integer client_id,
                       Model model) {
        final Optional<Plan> data = dataSet.plans.findById(plan.getId());
        final Optional<Employee> employee = dataSet.employees.findById(employee_id);
        final Optional<Client> client = dataSet.clients.findById(client_id);
        if (data.isPresent() && employee.isPresent() && client.isPresent()) {
            plan.setEmployee(employee.get());
            plan.setClient(client.get());
            dataSet.plans.save(plan);
            model.addAttribute("model", plan);
            return "redirect:/plan";
        }
        return "redirect:/notfound";
    }

    @GetMapping("delete")
    public String delete(@RequestParam(required = false) Integer id, Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Plan> plan = dataSet.plans.findById(id);
        if (plan.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", plan.get());
        return "plan/delete";
    }

    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam int id) {
        final Optional<Plan> plan = dataSet.plans.findById(id);
        if (plan.isEmpty()) return "redirect:/notfound";

        dataSet.plans.delete(plan.get());
        return "redirect:/plan";
    }
}