package com.crm.dao;


/**
 * A data set contains
 * {@link ClientRepository},
 * {@link CostRepository},
 * {@link EmployeeRepository},
 * {@link FeedbackRepository},
 * {@link PlanRepository},
 * {@link ProductRepository}.
 */
public class DataSet {
    /**
     * A client repository.
     */
    public final ClientRepository clients;
    /**
     * A cost repository.
     */
    public final CostRepository costs;
    /**
     * An employee repository.
     */
    public final EmployeeRepository employees;
    /**
     * A feedback repository.
     */
    public final FeedbackRepository feedbacks;
    /**
     * A plan repository.
     */
    public final PlanRepository plans;
    /**
     * A product repository.
     */
    public final ProductRepository products;

    /**
     * Constructor
     *
     * @param clients   a client repository
     * @param costs     a cost repository
     * @param employees an employee repository
     * @param feedbacks a feedback repository
     * @param plans     a plan repository
     * @param products  a product repository
     */
    public DataSet(ClientRepository clients,
                   CostRepository costs,
                   EmployeeRepository employees,
                   FeedbackRepository feedbacks,
                   PlanRepository plans,
                   ProductRepository products) {
        this.clients = clients;
        this.costs = costs;
        this.employees = employees;
        this.feedbacks = feedbacks;
        this.plans = plans;
        this.products = products;
    }
}
