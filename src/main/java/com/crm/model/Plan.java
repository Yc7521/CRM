package com.crm.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

import static com.crm.conf.Data.dateTimePattern;

@Entity
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private Employee employee;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "planned_profit", nullable = false)
    private Double plannedProfit;

    @Temporal(TemporalType.DATE)
    @Column(name = "planned_time", nullable = false)
    @DateTimeFormat(pattern = dateTimePattern)
    private Date plannedTime = new Date(); // set default value: now()

    @Column(name = "finished", nullable = false)
    private Boolean finished = false;

    @Enumerated
    @Column(name = "plan_state", nullable = false)
    private PlanState planState = PlanState.IN_PROGRESS;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Double getPlannedProfit() {
        return plannedProfit;
    }

    public void setPlannedProfit(Double plannedProfit) {
        this.plannedProfit = plannedProfit;
    }

    public Date getPlannedTime() {
        return plannedTime;
    }

    public void setPlannedTime(Date plannedTime) {
        this.plannedTime = plannedTime;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public PlanState getPlanState() {
        return planState;
    }

    public void setPlanState(PlanState planState) {
        this.planState = planState;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

}
