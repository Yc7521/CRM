package com.crm.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

import static com.crm.conf.Data.dateTimePattern;

/**
 * 反馈
 */
@SuppressWarnings("unused")
@Entity
public class Feedback {
    /**
     * 反馈编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * 反馈人姓名
     */
    @Column(name = "user_name", nullable = false, length = 20)
    private String userName;

    /**
     * 反馈时间
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "time", nullable = false)
    @DateTimeFormat(pattern = dateTimePattern)
    private Date time = new Date(); // set default value: now()

    /**
     * 反馈内容
     */
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * 产品
     */
    @ManyToOne(cascade = CascadeType.ALL,
            optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * 解决情况
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FeedbackStatus status = FeedbackStatus.PENDING;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public FeedbackStatus getStatus() {
        return status;
    }

    public void setStatus(FeedbackStatus status) {
        this.status = status;
    }
}
