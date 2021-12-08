package com.crm.controller;

import com.crm.dao.DataSet;
import com.crm.model.Client;
import com.crm.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.crm.conf.Data.maxSize;

@Controller
@RequestMapping("employee")
public class EmployeeController {
    private final DataSet dataSet;
    PasswordEncoder passwordEncoder;

    public EmployeeController(DataSet dataSet,
                              PasswordEncoder passwordEncoder) {
        this.dataSet = dataSet;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping({"", "/index"})
    public String index(@RequestParam(defaultValue = "0") int page,
                        Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        final Page<Employee> employeePage = dataSet.employees.findAll(id);
        model.addAttribute("model", employeePage);
        return "employee/index";
    }

    @GetMapping("search")
    public String search(@RequestParam(required = false) String search,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        if (search.isEmpty()) return "redirect:/employee";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.employees.findAllByNameContains(id, search));
        return "employee/index";
    }

    @GetMapping("create")
    public String create(Model model) {
        model.addAttribute("model", new Employee());
        return "employee/create";
    }

    @PostMapping("create")
    public String create(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        dataSet.employees.save(employee);
        return "redirect:/employee/";
    }

    @GetMapping("details")
    public String details(@RequestParam(required = false) Integer id,
                          Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Employee> employee = dataSet.employees.findById(id);
        if (employee.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", employee.get());
        return "employee/details";
    }

    @GetMapping("edit")
    public String edit(@RequestParam(required = false) Integer id,
                       Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Employee> employee = dataSet.employees.findById(id);
        if (employee.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", employee.get());
        return "employee/edit";
    }

    @PostMapping("edit")
    public String edit(Employee employee, Model model) {
        final Optional<Employee> data = dataSet.employees.findById(employee.getId());
        if (data.isPresent()) {
            final Employee employee1 = data.get();
            employee1.setName(employee.getName());
            employee1.setDepartment(employee.getDepartment());
            employee1.setSalary(employee.getSalary());
            employee1.setEmployeeType(employee.getEmployeeType());
            dataSet.employees.save(employee1);
            model.addAttribute("model", employee1);
            return "redirect:/employee";
        }
        return "redirect:/notfound";
    }

    @GetMapping("delete")
    public String delete(@RequestParam(required = false) Integer id,
                         Model model) {
        if (id == null) return "redirect:/notfound";

        final Optional<Employee> employee = dataSet.employees.findById(id);
        if (employee.isEmpty()) return "redirect:/notfound";

        model.addAttribute("model", employee.get());
        return "employee/delete";
    }

    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam int id) {
        final Optional<Employee> employee = dataSet.employees.findById(id);
        if (employee.isEmpty()) return "redirect:/notfound";

        dataSet.employees.delete(employee.get());
        return "redirect:/employee";
    }
}