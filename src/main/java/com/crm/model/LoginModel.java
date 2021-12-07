package com.crm.model;

import com.crm.conf.Data;

public class LoginModel {
    public String username;
    public String password;
    public String type;

    public LoginModel() {
    }

    public LoginModel(String username, String password, String type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentity() {
        return "%s%s%s".formatted(type, Data.split, username);
    }
}
