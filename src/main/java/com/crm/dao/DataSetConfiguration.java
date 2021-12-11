package com.crm.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A configuration class used to set the {@link DataSet} as a bean.
 */
@Configuration
public class DataSetConfiguration {
    /**
     * Registers the {@link DataSet} as a bean.
     *
     * @param clients   the {@link ClientRepository}
     * @param costs     the {@link CostRepository}
     * @param employees the {@link EmployeeRepository}
     * @param feedbacks the {@link FeedbackRepository}
     * @param plans     the {@link PlanRepository}
     * @param products  the {@link ProductRepository}
     * @return the {@link DataSet}
     */
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