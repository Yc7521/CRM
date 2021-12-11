package com.crm.dao;

import com.crm.model.Feedback;
import com.crm.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A {@link JpaRepository} for {@link Feedback}s.
 */
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    /**
     * Finds a {@link Page} of {@link Feedback}s by the given product.name.
     *
     * @param page the page request, which get by {@link PageRequest#of(int, int)}
     *             or {@link PageRequest#of(int, int, Sort)}
     * @param name the name of the {@link Product}
     * @return a {@link Page} of {@link Feedback}s
     */
    Page<Feedback> findAllByProduct_Name(Pageable page, String name);

    /**
     * Finds a {@link Page} of {@link Feedback}s by the given product.id.
     *
     * @param page the page request, which get by {@link PageRequest#of(int, int)}
     *             or {@link PageRequest#of(int, int, Sort)}
     * @param id   the id of the {@link Product}
     * @return a {@link Page} of {@link Feedback}s
     */
    Page<Feedback> findAllByProduct_Id(Pageable page, Integer id);
}