package com.crm.dao;

import com.crm.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findByName(String name);
    Page<Client> findAllByNameContains(Pageable pageable, String name);
}