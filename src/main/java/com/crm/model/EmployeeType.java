package com.crm.model;

import com.crm.conf.Data;

/**
 * 员工类型
 */
public enum EmployeeType {
    /**
     * 员工
     */
    EMPLOYEE(Data.employeeRoles, "员工"),
    /**
     * 经理
     */
    MANAGER(Data.managerRoles, "经理");

    /**
     * 拥有身份组
     */
    private final String[] roles;
    /**
     * 描述
     */
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