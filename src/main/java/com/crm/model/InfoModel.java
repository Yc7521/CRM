package com.crm.model;

/**
 * 用于显示信息模型
 */
public record InfoModel(String title, String content) {
    public InfoModel(String title, Object content) {
        this(title, content.toString());
    }
}
