package com.crm.model;

import com.crm.conf.Data;

public enum EmployeeType {
    EMPLOYEE(Data.employeeRoles), MANAGER(Data.managerRoles);

    private final String[] roles;

    EmployeeType(String[] roles) {
        this.roles = roles;
    }

    public String[] getRoles() {
        return roles;
    }
}