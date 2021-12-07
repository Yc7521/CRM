package com.crm.model;

import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
public class Employee implements User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 20)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "department", nullable = false, length = 64)
    private String department;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "employee",
            cascade = CascadeType.ALL)
    private Set<Cost> sellProducts = new java.util.LinkedHashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "employee",
            cascade = CascadeType.ALL)
    private Set<Plan> sellPlans = new java.util.LinkedHashSet<>();

    @NumberFormat(pattern = "000000.00", style = NumberFormat.Style.CURRENCY)
    @Column(name = "salary", nullable = false)
    private Double salary = .0; // set default value: 0

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
