package com.crm.dao;

import com.crm.model.Employee;
import com.crm.model.EmployeeType;
import com.crm.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    default Employee findManagerByName(String name) {
        return findByNameAndEmployeeType(name, EmployeeType.MANAGER);
    }

    default Employee findEmployeeByName(String name) {
        return findByNameAndEmployeeType(name, EmployeeType.EMPLOYEE);
    }

    Employee findByNameAndEmployeeType(String name, EmployeeType employeeType);

    Employee findByName(String name);

    Page<Employee> findAllByNameContains(PageRequest id, String name);
}