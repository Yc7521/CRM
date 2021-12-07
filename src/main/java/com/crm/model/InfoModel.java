package com.crm.model;

public record InfoModel(String title, String content) {
    public InfoModel(String title, Object content) {
        this(title, content.toString());
    }
}
