package com.crm.dao;

import com.crm.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A {@link JpaRepository} for {@link Product}s.
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {
    /**
     * Finds a {@link Page} of {@link Product}s by containing the given name.
     *
     * @param page the page request, which get by {@link PageRequest#of(int, int)}
     *             or {@link PageRequest#of(int, int, Sort)}
     * @param name the name of {@link Product}
     * @return a {@link Page} of {@link Product}s
     */
    Page<Product> findAllByNameContains(Pageable page, String name);
}