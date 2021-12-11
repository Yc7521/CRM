package com.crm.dao;

import com.crm.model.Cost;
import com.crm.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A {@link JpaRepository} for {@link Cost}s.
 */
public interface CostRepository extends JpaRepository<Cost, Integer> {
    /**
     * Finds a {@link Page} of {@link Cost}s by the given employee.name.
     *
     * @param page the page request, which get by {@link PageRequest#of(int, int)}
     *             or {@link PageRequest#of(int, int, Sort)}
     * @param name the name of the {@link Employee}
     * @return a {@link Page} of {@link Cost}s
     */
    Page<Cost> findAllByEmployee_Name(Pageable page, String name);

    /**
     * Finds a {@link Page} of {@link Cost}s by the given employee.id.
     *
     * @param page the page request, which get by {@link PageRequest#of(int, int)}
     *             or {@link PageRequest#of(int, int, Sort)}
     * @param id   the id of the {@link Employee}
     * @return a {@link Page} of {@link Cost}s
     */
    Page<Cost> findAllByEmployee_Id(Pageable page, Integer id);
}