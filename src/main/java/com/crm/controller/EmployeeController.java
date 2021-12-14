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
    public EmployeeController(final DataSet dataSet, final PasswordEncoder passwordEncoder) {
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
    static Employee getEmployee(final Integer id, final DataSet dataSet) {
        if (id == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Employee not found");

        final Optional<Employee> employee = dataSet.employees.findById(id);
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
    public String index(@RequestParam(defaultValue = "0") final int page, final Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        final Page<Employee> employeePage = dataSet.employees.findAll(id);
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
    public String search(@RequestParam(required = false) final String search,
                         @RequestParam(defaultValue = "0") final int page,
                         final Model model) {
        if (search.isEmpty()) return "redirect:/employee";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.employees.findAllByNameContains(id, search));
        return "employee/index";
    }

    /**
     * [GET]
     * mapping to /employee/create?id=0
     */
    @GetMapping("create")
    public void create(final Model model) {
        model.addAttribute("model", new Employee());
    }

    /**
     * [POST]
     * mapping to /employee/create
     *
     * @param employee the {@link Employee}
     */
    @PostMapping("create")
    public String create(final Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        dataSet.employees.save(employee);
        return "redirect:/employee";
    }

    /**
     * [GET]
     * mapping to /employee/details?id=0
     *
     * @param id the id of {@link Employee}
     */
    @GetMapping("details")
    public void details(@RequestParam(required = false) final Integer id, final Model model) {
        model.addAttribute("model", getEmployee(id, dataSet));
    }

    /**
     * [GET]
     * mapping to /employee/edit?id=0
     *
     * @param id the id of {@link Employee}
     */
    @GetMapping("edit")
    public void edit(@RequestParam(required = false) final Integer id, final Model model) {
        model.addAttribute("model", getEmployee(id, dataSet));
    }

    /**
     * [POST]
     * mapping to /employee/edit
     *
     * @param employee the {@link Employee}
     */
    @PostMapping("edit")
    public String edit(final Employee employee, final Model model) {
        // check if exists
        Employee employee1 = getEmployee(employee.getId(), dataSet);
        employee1.setName(employee.getName());
        employee1.setDepartment(employee.getDepartment());
        employee1.setSalary(employee.getSalary());
        employee1.setEmployeeType(employee.getEmployeeType());
        dataSet.employees.save(employee1);
        return "redirect:/employee";
    }

    /**
     * [GET]
     * mapping to /employee/resetPwd?id=0
     *
     * @param id the id of {@link Employee}
     */
    @GetMapping("resetPwd")
    public void resetPwd(@RequestParam(required = false) final Integer id, final Model model) {
        model.addAttribute("model", EmployeeController.getEmployee(id, dataSet));
    }

    /**
     * [POST]
     * mapping to /employee/resetPwd
     *
     * @param employee the {@link Employee}
     */
    @PostMapping("resetPwd")
    public String resetPwd(final Employee employee,
                           @RequestParam final String old_password,
                           final Model model) {
        // check if exists
        Employee employee1 = EmployeeController.getEmployee(employee.getId(), dataSet);
        if (this.passwordEncoder.matches(old_password, employee1.getPassword())) {
            employee1.setPassword(passwordEncoder.encode(employee.getPassword()));
            dataSet.employees.save(employee1);
        } else {
            model.addAttribute("model", EmployeeController.getEmployee(employee.getId(), dataSet));
            model.addAttribute("error", "请输入正确的密码");
            return "employee/resetPwd";
        }
        return "redirect:/employee";
    }

    /**
     * [GET]
     * mapping to /employee/delete?id=0
     *
     * @param id the id of {@link Employee}
     */
    @GetMapping("delete")
    public void delete(@RequestParam(required = false) final Integer id, final Model model) {
        model.addAttribute("model", getEmployee(id, dataSet));
    }

    /**
     * [POST]
     * mapping to /employee/delete
     *
     * @param id the id of {@link Employee}
     */
    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam final int id) {
        dataSet.employees.delete(getEmployee(id, dataSet));
        return "redirect:/employee";
    }
}