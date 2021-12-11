package com.crm.dao;

import com.crm.model.Employee;
import com.crm.model.EmployeeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A {@link JpaRepository} for {@link Employee}s.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    /**
     * Finds the {@link Employee} whose employeeType is equal to
     * {@link EmployeeType#MANAGER} by the given name.
     *
     * @param name the name of {@link Employee}
     * @return null if not found
     * @see EmployeeRepository#findByNameAndEmployeeType(String, EmployeeType)
     */
    default Employee findManagerByName(String name) {
        return findByNameAndEmployeeType(name, EmployeeType.MANAGER);
    }

    /**
     * Finds the {@link Employee} whose type is equal to
     * {@link EmployeeType#EMPLOYEE} by the given name.
     *
     * @param name the name of {@link Employee}
     * @return null if not found
     * @see EmployeeRepository#findByNameAndEmployeeType(String, EmployeeType)
     */
    default Employee findEmployeeByName(String name) {
        return findByNameAndEmployeeType(name, EmployeeType.EMPLOYEE);
    }

    /**
     * Finds the {@link Employee} by the given name and employeeType.
     *
     * @param name         the name of {@link Employee}
     * @param employeeType the employeeType of {@link Employee}
     * @return null if not found
     */
    Employee findByNameAndEmployeeType(String name, EmployeeType employeeType);

    /**
     * Finds a {@link Page} of {@link Employee}s by containing the given name.
     *
     * @param page the page request, which get by {@link PageRequest#of(int, int)}
     *             or {@link PageRequest#of(int, int, Sort)}
     * @param name the name of {@link Employee}
     * @return a {@link Page} of {@link Employee}s
     */
    Page<Employee> findAllByNameContains(Pageable page, String name);
}