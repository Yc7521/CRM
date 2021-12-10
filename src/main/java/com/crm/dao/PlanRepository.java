package com.crm.dao;

import com.crm.model.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Integer> {
    Page<Plan> findAllByClient_Name(Pageable id, String name);

    Page<Plan> findAllByClient_Id(Pageable id, Integer id1);

    Page<Plan> findAllByEmployee_Id(Pageable id, Integer id1);
}