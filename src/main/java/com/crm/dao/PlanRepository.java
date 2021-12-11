package com.crm.dao;

import com.crm.model.Client;
import com.crm.model.Employee;
import com.crm.model.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A {@link JpaRepository} for {@link Plan}s.
 */
public interface PlanRepository extends JpaRepository<Plan, Integer> {
    /**
     * Finds a {@link Page} of {@link Plan}s by the given client.name.
     *
     * @param page the page request, which get by {@link PageRequest#of(int, int)}
     *             or {@link PageRequest#of(int, int, Sort)}
     * @param name the name of {@link Client}
     * @return a {@link Page} of {@link Plan}s
     */
    Page<Plan> findAllByClient_Name(Pageable page, String name);

    /**
     * Finds a {@link Page} of {@link Plan}s by the given client.id.
     *
     * @param page the page request, which get by {@link PageRequest#of(int, int)}
     *             or {@link PageRequest#of(int, int, Sort)}
     * @param id   an id of {@link Client}
     * @return a {@link Page} of {@link Plan}s
     */
    Page<Plan> findAllByClient_Id(Pageable page, Integer id);

    /**
     * Finds a {@link Page} of {@link Plan}s by the given employee.id.
     *
     * @param page the page request, which get by {@link PageRequest#of(int, int)}
     *             or {@link PageRequest#of(int, int, Sort)}
     * @param id   the id of {@link Employee}
     * @return a {@link Page} of {@link Plan}s
     */
    Page<Plan> findAllByEmployee_Id(Pageable page, Integer id);
}