package com.swapnil.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.swapnil.model.Employee;

public interface EmployeeService {


    Optional<Employee> getEmployeeById(long id);

    Employee addEmployee(Employee employee);

    Optional<Employee> updateEmployee(Employee employee);

    boolean deleteEmployee(long id);

	/**
	 * Retrieves all employees from the database with sorting and pagination.
	 *
	 * @param page     Page number (starting from 0)
	 * @param pageSize Number of items per page
	 * @param sortBy   Sorting field
	 * @param sortOrder Sorting order (ASC or DESC)
	 * @return Page of employees
	 */
	Page<Employee> getAllEmployees(int page, int pageSize, String sortBy, String sortOrder);

	List<Employee> getAllEmployees();
}
