package com.crm.dao;

import com.crm.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A {@link JpaRepository} for {@link Client}s.
 */
public interface ClientRepository extends JpaRepository<Client, Integer> {
    /**
     * Finds a {@link Client} by the given name.
     *
     * @param name the name of the {@link Client}
     * @return null if no {@link Client} with the given name exists, otherwise the {@link Client}
     */
    Client findByName(String name);

    /**
     * Finds a {@link Page} of {@link Client} by containing the given name.
     *
     * @param page the page request, which get by {@link PageRequest#of(int, int)}
     *             or {@link PageRequest#of(int, int, Sort)}
     * @param name the name of the {@link Client}
     * @return a {@link Page} of {@link Client}s
     */
    Page<Client> findAllByNameContains(Pageable page, String name);
}