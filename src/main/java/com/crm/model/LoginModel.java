package com.crm.model;

import com.crm.conf.Data;

/**
 * 登录界面模型
 */
@SuppressWarnings("unused")
public class LoginModel {
    /**
     * 用户名
     */
    public String username;
    /**
     * 密码
     */
    public String password;
    /**
     * 用户类型
     */
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
