package com.crm.model;

import com.crm.conf.Data;

import javax.persistence.*;

@Entity
public class Client implements User {
    /**
     * 编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 20)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "tel", nullable = false, length = 20)
    private String tel;

    @Column(name = "credit", nullable = false)
    private Integer credit = 100; // set default value: 100

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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
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
        return Data.clientRoles;
    }
}
