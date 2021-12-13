package com.crm.controller;

import com.crm.dao.DataSet;
import com.crm.model.*;
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

/**
 * A controller for the plan page.
 */
@Controller
@RequestMapping("plan")
public class PlanController {
    private final DataSet dataSet;

    /**
     * Constructor
     *
     * @param dataSet a {@link DataSet}
     */
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

        Optional<Plan> plan = dataSet.plans.findById(id);
        if (plan.isEmpty())
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Plan not found");

        return plan.get();
    }

    /**
     * [GET]
     * mapping to /plan/[index]?page=0
     *
     * @param page a number of page
     */
    @GetMapping({"", "index"})
    public String index(@RequestParam(defaultValue = "0") int page, Model model) {
        PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        Page<Plan> planPage = this.dataSet.plans.findAll(id);
        model.addAttribute("model", planPage);
        return "plan/index";
    }

    /**
     * [GET]
     * mapping to /plan/search?page=0&search=
     *
     * @param search the name of {@link Client}
     * @param page   a number of page
     */
    @GetMapping("search")
    public String search(@RequestParam(required = false) String search,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        if (search.isEmpty()) return "redirect:/plan";
        PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", this.dataSet.plans.findAllByClient_Name(id, search));
        return "plan/index";
    }

    /**
     * [GET]
     * mapping to /plan/searchClientId?page=0&search=
     *
     * @param search the id of {@link Client}
     * @param page   a number of page
     */
    @GetMapping("searchClientId")
    public String searchClientId(@RequestParam(required = false) Integer search,
                                 @RequestParam(defaultValue = "0") int page,
                                 Model model) {
        if (search == null) return "redirect:/plan";
        PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", this.dataSet.plans.findAllByClient_Id(id, search));
        return "plan/index";
    }

    /**
     * [GET]
     * mapping to /plan/searchEmployeeId?page=0&search=
     *
     * @param search the id of {@link Employee}
     * @param page   a number of page
     */
    @GetMapping("searchEmployeeId")
    public String searchEmployeeId(@RequestParam(required = false) Integer search,
                                   @RequestParam(defaultValue = "0") int page,
                                   Model model) {
        if (search == null) return "redirect:/plan";
        PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", this.dataSet.plans.findAllByEmployee_Id(id, search));
        return "plan/index";
    }

    /**
     * [GET]
     * mapping to /plan/create?id=0
     *
     * @param client_id   the id of {@link Client}
     * @param employee_id the id of {@link Employee}
     */
    @GetMapping("create")
    public void create(@RequestParam(required = false) Integer employee_id,
                       @RequestParam(required = false) Integer client_id,
                       Model model) {
        Plan plan = new Plan();
        if (employee_id != null)
            plan.setEmployee(EmployeeController.getEmployee(employee_id, this.dataSet));
        if (client_id != null)
            plan.setClient(ClientController.getClient(client_id, this.dataSet));

        model.addAttribute("model", plan);
        model.addAttribute("employees", this.dataSet.employees.findAll());
        model.addAttribute("clients", this.dataSet.clients.findAll());
    }

    /**
     * [POST]
     * mapping to /plan/create
     *
     * @param plan        the {@link Plan}
     * @param client_id   the id of {@link Client}
     * @param employee_id the id of {@link Employee}
     */
    @PostMapping("create")
    public String create(Plan plan,
                         @RequestParam Integer employee_id,
                         @RequestParam Integer client_id) {
        plan.setEmployee(EmployeeController.getEmployee(employee_id, this.dataSet));
        plan.setClient(ClientController.getClient(client_id, this.dataSet));
        this.dataSet.plans.save(plan);
        return "redirect:/plan";
    }

    /**
     * [GET]
     * mapping to /plan/details?id=0
     *
     * @param id the id of {@link Plan}
     */
    @GetMapping("details")
    public void details(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", PlanController.getPlan(id, this.dataSet));
    }

    /**
     * [GET]
     * mapping to /plan/edit?id=0
     *
     * @param id the id of {@link Plan}
     */
    @GetMapping("edit")
    public void edit(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", PlanController.getPlan(id, this.dataSet));
        model.addAttribute("employees", this.dataSet.employees.findAll());
        model.addAttribute("clients", this.dataSet.clients.findAll());
    }

    /**
     * [POST]
     * mapping to /plan/edit
     *
     * @param plan        the {@link Plan}
     * @param client_id   the id of {@link Client}
     * @param employee_id the id of {@link Employee}
     */
    @PostMapping("edit")
    public String edit(Plan plan,
                       @RequestParam Integer employee_id,
                       @RequestParam Integer client_id,
                       Model model) {
        // check if exists
        final Plan plan1 = PlanController.getPlan(plan.getId(), this.dataSet);
        plan1.setEmployee(EmployeeController.getEmployee(employee_id, this.dataSet));
        plan1.setClient(ClientController.getClient(client_id, this.dataSet));
        plan1.setPlannedProfit(plan.getPlannedProfit());
        plan1.setFinished(plan.getFinished());
        plan1.setPlanState(plan.getPlanState());
        this.dataSet.plans.save(plan1);
        return "redirect:/plan";
    }

    /**
     * [GET]
     * mapping to /plan/delete?id=0
     *
     * @param id the id of {@link Plan}
     */
    @GetMapping("delete")
    public void delete(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("model", PlanController.getPlan(id, this.dataSet));
    }

    /**
     * [POST]
     * mapping to /plan/delete
     *
     * @param id the id of {@link Plan}
     */
    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam int id) {
        this.dataSet.plans.delete(PlanController.getPlan(id, this.dataSet));
        return "redirect:/plan";
    }
}