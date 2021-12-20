package com.crm.dao;

import com.crm.model.Client;
import com.crm.model.Cost;
import com.crm.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    /**
     * Finds a {@link Page} of {@link Cost}s by the given client.id.
     *
     * @param page     the page request, which get by {@link PageRequest#of(int, int)}
     *                 or {@link PageRequest#of(int, int, Sort)}
     * @param id       the id of the {@link Client}
     * @param employee not the {@link Employee}
     * @return a {@link Page} of {@link Cost}s
     */
    @Query("select c from Cost c where c.client.id = ?1 and c.employee <> ?2")
    Page<Cost> findAllByClient_IdAndEmployeeNot(Pageable page,
                                                Integer id,
                                                Employee employee);

    /**
     * Finds a {@link Page} of {@link Cost}s by the given client.id.
     *
     * @param page     the page request, which get by {@link PageRequest#of(int, int)}
     *                 or {@link PageRequest#of(int, int, Sort)}
     * @param client   the id of the {@link Client}
     * @param employee the name of {@link Employee}
     * @return a {@link Page} of {@link Cost}s
     */
    Page<Cost> findAllByClient_IdAndEmployee_Name(Pageable page,
                                                  Integer client,
                                                  String employee);
}