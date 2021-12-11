package com.crm.controller;

import com.crm.dao.DataSet;
import com.crm.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static com.crm.conf.Data.maxSize;

@Controller
@RequestMapping("employee")
public class EmployeeController {
    private final DataSet dataSet;
    private final PasswordEncoder passwordEncoder;

    public EmployeeController(DataSet dataSet, PasswordEncoder passwordEncoder) {
        this.dataSet = dataSet;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get {@link Employee} by id
     *
     * @param id      the id of {@link Employee}
     * @param dataSet data set
     * @throws HttpClientErrorException if {@link Employee} not found
     */
    static Employee getEmployee(Integer id, DataSet dataSet) {
        if (id == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Employee not found");

        final Optional<Employee> employee = dataSet.employees.findById(id);
        if (employee.isEmpty())
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Employee not found");

        return employee.get();
    }

    @GetMapping({"", "/index"})
    public void index(@RequestParam(defaultValue = "0") int page, Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        final Page<Employee> employeePage = dataSet.employees.findAll(id);
        model.addAttribute("model", employeePage);
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
    public void create(Model model) {
        model.addAttribute("model", new Employee());
    }

    @PostMapping("create")
    public String create(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        dataSet.employees.save(employee);
        return "redirect:/employee";
    }

    @GetMapping("details")
    public void details(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", getEmployee(id, dataSet));
    }

    @GetMapping("edit")
    public void edit(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", getEmployee(id, dataSet));
    }

    @PostMapping("edit")
    public String edit(Employee employee, Model model) {
        // check if exists
        getEmployee(employee.getId(), dataSet);
        dataSet.employees.save(employee);
        return "redirect:/employee";
    }

    @GetMapping("delete")
    public void delete(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", getEmployee(id, dataSet));
    }

    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam int id) {
        dataSet.employees.delete(getEmployee(id, dataSet));
        return "redirect:/employee";
    }
}