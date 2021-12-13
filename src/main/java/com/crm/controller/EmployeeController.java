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

/**
 * A controller for the employee page.
 */
@Controller
@RequestMapping("employee")
public class EmployeeController {
    private final DataSet dataSet;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor
     *
     * @param dataSet a {@link DataSet}
     */
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

        Optional<Employee> employee = dataSet.employees.findById(id);
        if (employee.isEmpty())
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Employee not found");

        return employee.get();
    }

    /**
     * [GET]
     * mapping to /employee/[index]?page=0
     *
     * @param page a number of page
     */
    @GetMapping({"", "index"})
    public String index(@RequestParam(defaultValue = "0") int page, Model model) {
        PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        Page<Employee> employeePage = this.dataSet.employees.findAll(id);
        model.addAttribute("model", employeePage);
        return "employee/index";
    }

    /**
     * [GET]
     * mapping to /employee/search?page=0&search=
     *
     * @param search the name of {@link Employee}
     * @param page   a number of page
     */
    @GetMapping("search")
    public String search(@RequestParam(required = false) String search,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        if (search.isEmpty()) return "redirect:/employee";
        PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", this.dataSet.employees.findAllByNameContains(id, search));
        return "employee/index";
    }

    /**
     * [GET]
     * mapping to /employee/create?id=0
     */
    @GetMapping("create")
    public void create(Model model) {
        model.addAttribute("model", new Employee());
    }

    /**
     * [POST]
     * mapping to /employee/create
     *
     * @param employee the {@link Employee}
     */
    @PostMapping("create")
    public String create(Employee employee) {
        employee.setPassword(this.passwordEncoder.encode(employee.getPassword()));
        this.dataSet.employees.save(employee);
        return "redirect:/employee";
    }

    /**
     * [GET]
     * mapping to /employee/details?id=0
     *
     * @param id the id of {@link Employee}
     */
    @GetMapping("details")
    public void details(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", EmployeeController.getEmployee(id, this.dataSet));
    }

    /**
     * [GET]
     * mapping to /employee/edit?id=0
     *
     * @param id the id of {@link Employee}
     */
    @GetMapping("edit")
    public void edit(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", EmployeeController.getEmployee(id, this.dataSet));
    }

    /**
     * [POST]
     * mapping to /employee/edit
     *
     * @param employee the {@link Employee}
     */
    @PostMapping("edit")
    public String edit(Employee employee, Model model) {
        // check if exists
        final Employee employee1 = EmployeeController.getEmployee(employee.getId(), this.dataSet);
        employee1.setName(employee.getName());
        employee1.setDepartment(employee.getDepartment());
        employee1.setSalary(employee.getSalary());
        employee1.setEmployeeType(employee.getEmployeeType());
        this.dataSet.employees.save(employee1);
        return "redirect:/employee";
    }

    /**
     * [GET]
     * mapping to /employee/delete?id=0
     *
     * @param id the id of {@link Employee}
     */
    @GetMapping("delete")
    public void delete(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", EmployeeController.getEmployee(id, this.dataSet));
    }

    /**
     * [POST]
     * mapping to /employee/delete
     *
     * @param id the id of {@link Employee}
     */
    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam int id) {
        this.dataSet.employees.delete(EmployeeController.getEmployee(id, this.dataSet));
        return "redirect:/employee";
    }
}