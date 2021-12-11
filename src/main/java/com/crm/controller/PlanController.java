package com.crm.controller;

import com.crm.dao.DataSet;
import com.crm.model.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
@RequestMapping("plan")
public class PlanController {
    private final DataSet dataSet;

    public PlanController(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * Get {@link Plan} by id
     *
     * @param id      the id of {@link Plan}
     * @param dataSet data set
     * @throws HttpClientErrorException if {@link Plan} not found
     */
    static Plan getPlan(Integer id, DataSet dataSet) {
        if (id == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Plan not found");

        final Optional<Plan> plan = dataSet.plans.findById(id);
        if (plan.isEmpty())
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Plan not found");

        return plan.get();
    }

    @GetMapping({"", "/index"})
    public void index(@RequestParam(defaultValue = "0") int page, Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        final Page<Plan> planPage = dataSet.plans.findAll(id);
        model.addAttribute("model", planPage);
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
     */
    @GetMapping("create")
    public void create(@RequestParam(required = false) Integer employee_id,
                         @RequestParam(required = false) Integer client_id,
                         Model model) {
        final Plan plan = new Plan();
        if (employee_id != null)
            plan.setEmployee(EmployeeController.getEmployee(employee_id, dataSet));
        if (client_id != null)
            plan.setClient(ClientController.getClient(client_id, dataSet));

        model.addAttribute("model", plan);
        model.addAttribute("employees", dataSet.employees.findAll());
        model.addAttribute("clients", dataSet.clients.findAll());
    }

    @PostMapping("create")
    public String create(Plan plan,
                         @RequestParam Integer employee_id,
                         @RequestParam Integer client_id) {
        plan.setEmployee(EmployeeController.getEmployee(employee_id, dataSet));
        plan.setClient(ClientController.getClient(client_id, dataSet));
        dataSet.plans.save(plan);
        return "redirect:/plan";
    }

    @GetMapping("details")
    public void details(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", getPlan(id, dataSet));
    }

    @GetMapping("edit")
    public void edit(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", getPlan(id, dataSet));
        model.addAttribute("employees", dataSet.employees.findAll());
        model.addAttribute("clients", dataSet.clients.findAll());
    }

    @PostMapping("edit")
    public String edit(Plan plan,
                       @RequestParam Integer employee_id,
                       @RequestParam Integer client_id,
                       Model model) {
        // check if exists
        getPlan(plan.getId(), dataSet);
        plan.setEmployee(EmployeeController.getEmployee(employee_id, dataSet));
        plan.setClient(ClientController.getClient(client_id, dataSet));
        dataSet.plans.save(plan);
        return "redirect:/plan";
    }

    @GetMapping("delete")
    public void delete(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", getPlan(id, dataSet));
    }

    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam int id) {
        dataSet.plans.delete(getPlan(id, dataSet));
        return "redirect:/plan";
    }
}