package com.crm.model;

import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "time", nullable = false)
    private Date time = new Date(); // set default value: now()

    @Column(name = "type", nullable = false, length = 20)
    private String type;

    @NumberFormat(pattern = "0000.00", style = NumberFormat.Style.CURRENCY)
    @Column(name = "price", nullable = false)
    private Double price = .0; // set default value: 0

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
}
