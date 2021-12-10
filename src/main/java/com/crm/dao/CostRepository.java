package com.crm.dao;

import com.crm.model.Cost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CostRepository extends JpaRepository<Cost, Integer> {
    Page<Cost> findAllByEmployee_Name(Pageable id, String name);

    Page<Cost> findAllByEmployee_Id(Pageable id, Integer id1);
}