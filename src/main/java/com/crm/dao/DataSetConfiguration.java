package com.crm.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSetConfiguration {
    @Bean
    public DataSet dataSet(ClientRepository clients,
                           CostRepository costs,
                           EmployeeRepository employees,
                           FeedbackRepository feedbacks,
                           PlanRepository plans,
                           ProductRepository products) {
        return new DataSet(clients, costs, employees, feedbacks, plans, products);
    }
}