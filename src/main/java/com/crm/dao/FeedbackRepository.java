package com.crm.dao;

import com.crm.model.Employee;
import com.crm.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    Page<Feedback> findAllByProduct_Name(Pageable pageable, String name);

    Page<Feedback> findAllByProduct_Id(Pageable id, Integer id1);
}