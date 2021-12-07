package com.crm.dao;

public class DataSet {
    public ClientRepository clients;
    public CostRepository costs;
    public EmployeeRepository employees;
    public FeedbackRepository feedbacks;
    public PlanRepository plans;
    public ProductRepository products;

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
