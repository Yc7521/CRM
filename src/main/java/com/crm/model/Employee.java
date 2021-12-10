package com.crm.model;

import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

/**
 * 员工 (普通员工或经理)
 */
@Entity
public class Employee implements User {
    /**
     * 员工编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * 员工姓名
     */
    @Column(name = "name", nullable = false, unique = true, length = 20)
    private String name;

    /**
     * 员工密码
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * 所属部门
     */
    @Column(name = "department", nullable = false, length = 64)
    private String department;

    /**
     * 产品 (映射, 非表中数据)
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "employee",
            cascade = CascadeType.ALL)
    private Set<Cost> sellProducts = new java.util.LinkedHashSet<>();

    /**
     * 任务计划 (映射, 非表中数据)
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "employee",
            cascade = CascadeType.ALL)
    private Set<Plan> sellPlans = new java.util.LinkedHashSet<>();

    /**
     * 工资
     */
    @NumberFormat(pattern = "000000.00", style = NumberFormat.Style.CURRENCY)
    @Column(name = "salary", nullable = false)
    private Double salary = .0; // set default value: 0

    /**
     * 员工类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "employee_type", nullable = false)
    private EmployeeType employeeType = EmployeeType.EMPLOYEE;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Set<Cost> getSellProducts() {
        return sellProducts;
    }

    public void setSellProducts(Set<Cost> sellProducts) {
        this.sellProducts = sellProducts;
    }

    public Set<Plan> getSellPlans() {
        return sellPlans;
    }

    public void setSellPlans(Set<Plan> sellPlans) {
        this.sellPlans = sellPlans;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String[] getRoles() {
        return employeeType.getRoles();
    }
}
