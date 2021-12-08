package com.crm.conf;

public class Data {
    public final static int maxSize = 20;
    public final static String split = "\n";
    public final static String dateTimePattern = "yyyy-MM-dd";


    public final static String[] roles = {"client", "employee", "manager"};
    public final static String[] clientRoles = {roles[0]};
    public final static String[] employeeRoles = {roles[0], roles[1]};
    public final static String[] managerRoles = {roles[0], roles[1], roles[2]};
}
