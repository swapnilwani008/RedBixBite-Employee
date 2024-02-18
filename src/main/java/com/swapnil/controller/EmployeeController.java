package com.swapnil.controller;

import com.swapnil.exception.EmailFoundException;
import com.swapnil.exception.EmployeeNotFoundException;
import com.swapnil.model.Employee;
import com.swapnil.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    private static final Logger LOGGER = Logger.getLogger(EmployeeController.class.getName());
    
    // Retrieve a list of all employees
    @GetMapping("/")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // Retrieve paginated and sorted list of employees
    @GetMapping("/sorted")
    public ResponseEntity<Page<Employee>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortOrder) {
        Page<Employee> employees = employeeService.getAllEmployees(page, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(employees);
    }

    // Retrieve an employee by ID or handle EmployeeNotFoundException
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable("id") long id) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(employee.orElse(null));
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID: " + id);
        }
    }

    // Add a new employee or handle EmailFoundException
    @PostMapping("/")
    public ResponseEntity<?> addEmployee(@Valid @RequestBody Employee employee) {
        try {
            Employee addedEmployee = employeeService.addEmployee(employee);
            LOGGER.info("Employee added: " + addedEmployee.toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(addedEmployee);
        } catch (EmailFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists: " + employee.getEmail());
        }
    }

    // Update an existing employee or handle EmployeeNotFoundException
    @PutMapping("/")
    public ResponseEntity<?> updateEmployee(@Valid @RequestBody Employee employee) {
        try {
            Optional<Employee> updatedEmployee = employeeService.updateEmployee(employee);
            LOGGER.info("Employee updated: " + updatedEmployee.orElse(null));
            return ResponseEntity.ok(updatedEmployee.orElse(null));
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID: " + employee.getEmployeeId());
        }
    }

    // Delete an employee by ID or handle EmployeeNotFoundException
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") long id) {
        try {
            employeeService.deleteEmployee(id);
            LOGGER.info("Employee deleted with ID: " + id);
            return ResponseEntity.ok("Employee deleted successfully");
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID: " + id);
        }
    }
}
