package com.crm.model;

import com.crm.conf.Data;

public enum EmployeeType {
    EMPLOYEE(Data.employeeRoles, "员工"), MANAGER(Data.managerRoles, "经理");

    private final String[] roles;
    private final String des;

    EmployeeType(String[] roles, String des) {
        this.roles = roles;
        this.des = des;
    }

    public String[] getRoles() {
        return roles;
    }

    public String getDes() {
        return des;
    }
}