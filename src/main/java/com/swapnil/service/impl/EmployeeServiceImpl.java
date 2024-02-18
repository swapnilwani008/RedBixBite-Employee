package com.swapnil.service.impl;

import com.swapnil.exception.EmployeeNotFoundException;
import com.swapnil.model.Employee;
import com.swapnil.repository.EmployeeRepository;
import com.swapnil.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final Logger LOGGER = Logger.getLogger(EmployeeServiceImpl.class.getName());

    // Retrieve paginated and sorted list of employees
    @Override
    public Page<Employee> getAllEmployees(int page, int pageSize, String sortBy, String sortOrder) {
        Sort.Direction sortDirection = Sort.Direction.fromString(sortOrder);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortDirection, sortBy));
        return employeeRepository.findAll(pageable);
    }

    // Retrieve employee by ID or throw an exception if not found
    @Override
    public Optional<Employee> getEmployeeById(long id) {
        return employeeRepository.findById(id)
                .or(() -> {
                    throw new EmployeeNotFoundException("Employee not found with ID: " + id);
                });
    }

    // Add a new employee to the database
    @Override
    public Employee addEmployee(Employee employee) {
        Employee addedEmployee = employeeRepository.save(employee);
        LOGGER.info(() -> "Employee added: " + addedEmployee.toString());
        return addedEmployee;
    }

    // Update an existing employee or throw an exception if not found
    @Override
    public Optional<Employee> updateEmployee(Employee employee) {
        long id = employee.getEmployeeId();
        return employeeRepository.existsById(id) ?
                Optional.of(employeeRepository.save(employee))
                : Optional.<Employee>empty()
                        .or(() -> {
                            throw new EmployeeNotFoundException("Employee not found with ID: " + id);
                        });
    }

    // Delete an employee by ID or throw an exception if not found
    @Override
    public boolean deleteEmployee(long id) {
        return Optional.of(employeeRepository.existsById(id))
                .filter(exists -> exists)
                .map(exists -> {
                    employeeRepository.deleteById(id);
                    LOGGER.info(() -> "Employee deleted with ID: " + id);
                    return true;
                })
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + id));
    }

    // Retrieve a list of all employees
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
