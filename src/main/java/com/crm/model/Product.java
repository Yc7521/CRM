package com.crm.model;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.crm.conf.Data.dateTimePattern;

/**
 * 产品
 */
@Entity
public class Product {
    /**
     * 产品编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * 产品名称
     */
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    /**
     * 生产日期
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "time", nullable = false)
    @DateTimeFormat(pattern = dateTimePattern)
    private Date time = new Date(); // set default value: now()

    /**
     * 产品类型
     */
    @Column(name = "type", nullable = false, length = 20)
    private String type;

    /**
     * 价格
     */
    @NumberFormat(pattern = "0000.00", style = NumberFormat.Style.CURRENCY)
    @Column(name = "price", nullable = false)
    private Double price = .0; // set default value: 0

    /**
     * 产品反馈信息
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
            mappedBy = "product")
    private Set<Feedback> feedbacks = new LinkedHashSet<>();

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Set<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
