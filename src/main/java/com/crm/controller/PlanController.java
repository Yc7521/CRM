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
    public PlanController(final DataSet dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * Get {@link Plan} by id
     *
     * @param id      the id of {@link Plan}
     * @param dataSet data set
     * @throws HttpClientErrorException if {@link Plan} not found
     */
    static Plan getPlan(final Integer id, final DataSet dataSet) {
        if (id == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Plan not found");

        final Optional<Plan> plan = dataSet.plans.findById(id);
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
    public String index(@RequestParam(defaultValue = "0") final int page, final Model model) {
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        final Page<Plan> planPage = dataSet.plans.findAll(id);
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
    public String search(@RequestParam(required = false) final String search,
                         @RequestParam(defaultValue = "0") final int page,
                         final Model model) {
        if (search.isEmpty()) return "redirect:/plan";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.plans.findAllByClient_Name(id, search));
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
    public String searchClientId(@RequestParam(required = false) final Integer search,
                                 @RequestParam(defaultValue = "0") final int page,
                                 final Model model) {
        if (search == null) return "redirect:/plan";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.plans.findAllByClient_Id(id, search));
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
    public String searchEmployeeId(@RequestParam(required = false) final Integer search,
                                   @RequestParam(defaultValue = "0") final int page,
                                   final Model model) {
        if (search == null) return "redirect:/plan";
        final PageRequest id = PageRequest.of(page, maxSize, Sort.by("id"));
        model.addAttribute("model", dataSet.plans.findAllByEmployee_Id(id, search));
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
    public void create(@RequestParam(required = false) final Integer employee_id,
                       @RequestParam(required = false) final Integer client_id,
                       final Model model) {
        final Plan plan = new Plan();
        if (employee_id != null)
            plan.setEmployee(EmployeeController.getEmployee(employee_id, dataSet));
        if (client_id != null)
            plan.setClient(ClientController.getClient(client_id, dataSet));

        model.addAttribute("model", plan);
        model.addAttribute("employees", dataSet.employees.findAll());
        model.addAttribute("clients", dataSet.clients.findAll());
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
    public String create(final Plan plan,
                         @RequestParam final Integer employee_id,
                         @RequestParam final Integer client_id) {
        plan.setEmployee(EmployeeController.getEmployee(employee_id, dataSet));
        plan.setClient(ClientController.getClient(client_id, dataSet));
        dataSet.plans.save(plan);
        return "redirect:/plan";
    }

    /**
     * [GET]
     * mapping to /plan/details?id=0
     *
     * @param id the id of {@link Plan}
     */
    @GetMapping("details")
    public void details(@RequestParam(required = false) final Integer id, final Model model) {
        model.addAttribute("model", getPlan(id, dataSet));
    }

    /**
     * [GET]
     * mapping to /plan/edit?id=0
     *
     * @param id the id of {@link Plan}
     */
    @GetMapping("edit")
    public void edit(@RequestParam(required = false) final Integer id, final Model model) {
        model.addAttribute("model", getPlan(id, dataSet));
        model.addAttribute("employees", dataSet.employees.findAll());
        model.addAttribute("clients", dataSet.clients.findAll());
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
    public String edit(final Plan plan,
                       @RequestParam final Integer employee_id,
                       @RequestParam final Integer client_id,
                       final Model model) {
        // check if exists
        Plan plan1 = getPlan(plan.getId(), dataSet);
        plan1.setEmployee(EmployeeController.getEmployee(employee_id, dataSet));
        plan1.setClient(ClientController.getClient(client_id, dataSet));
        plan1.setPlannedProfit(plan.getPlannedProfit());
        plan1.setFinished(plan.getFinished());
        plan1.setPlanState(plan.getPlanState());
        dataSet.plans.save(plan1);
        return "redirect:/plan";
    }

    /**
     * [GET]
     * mapping to /plan/delete?id=0
     *
     * @param id the id of {@link Plan}
     */
    @GetMapping("delete")
    public void delete(@RequestParam(required = false) final Integer id, final Model model) {
        model.addAttribute("model", getPlan(id, dataSet));
    }

    /**
     * [POST]
     * mapping to /plan/delete
     *
     * @param id the id of {@link Plan}
     */
    @PostMapping("delete")
    public String deleteConfirmed(@RequestParam final int id) {
        dataSet.plans.delete(getPlan(id, dataSet));
        return "redirect:/plan";
    }
}