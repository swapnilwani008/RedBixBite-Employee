package com.swapnil.controller;

import com.swapnil.exception.EmailFoundException;
import com.swapnil.exception.EmployeeNotFoundException;
import com.swapnil.model.Employee;
import com.swapnil.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    void testGetAllEmployees() {
        // Arrange
        List<Employee> mockEmployees = List.of(
                new Employee(1,"John", "Doe", "john@example.com", "Manager"),
                new Employee(2,"Jane", "Doe", "jane@example.com", "Developer")
        );
        
        // Create a Page<Employee> using PageImpl
        Page<Employee> mockPage = new PageImpl<>(mockEmployees, PageRequest.of(0, 10), mockEmployees.size());

        when(employeeService.getAllEmployees(0, 10, "employeeId", "ASC")).thenReturn(mockPage);

        // Act
        ResponseEntity<Page<Employee>> responseEntity = employeeController.getAllEmployees(0, 10, "employeeId", "ASC");

        // Assert
        assertEquals(mockEmployees, responseEntity.getBody().getContent());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetEmployee() {
        // Arrange
        long id = 1L;
        Employee mockEmployee = new Employee(id, "John", "Doe", "john@example.com", "Manager");
        when(employeeService.getEmployeeById(id)).thenReturn(Optional.of(mockEmployee));

        // Act
        ResponseEntity<?> responseEntity = employeeController.getEmployee(id);

        // Assert
        assertEquals(mockEmployee, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetEmployeeNotFound() {
        // Arrange
        long id = 1L;
        when(employeeService.getEmployeeById(id)).thenThrow(EmployeeNotFoundException.class);

        // Act
        ResponseEntity<?> responseEntity = employeeController.getEmployee(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testAddEmployee() {
        // Arrange
        Employee mockEmployee = new Employee(1,"John", "Doe", "john@example.com", "Manager");
        when(employeeService.addEmployee(mockEmployee)).thenReturn(mockEmployee);

        // Act
        ResponseEntity<?> responseEntity = employeeController.addEmployee(mockEmployee);

        // Assert
        assertEquals(mockEmployee, responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testAddEmployeeEmailConflict() {
        // Arrange
        Employee mockEmployee = new Employee(1, "John", "Doe", "john@example.com", "Manager");
        when(employeeService.addEmployee(mockEmployee)).thenThrow(EmailFoundException.class);

        // Act
        ResponseEntity<?> responseEntity = employeeController.addEmployee(mockEmployee);

        // Assert
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateEmployee() {
        // Arrange
        Employee existingEmployee = new Employee(1, "John", "Doe", "john@example.com", "Manager");
        when(employeeService.updateEmployee(existingEmployee)).thenReturn(Optional.of(existingEmployee));

        // Act
        ResponseEntity<?> responseEntity = employeeController.updateEmployee(existingEmployee);

        // Assert
        assertEquals(existingEmployee, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateEmployeeNotFound() {
        // Arrange
        Employee nonExistingEmployee = new Employee(1, "John", "Doe", "john@example.com", "Manager");
        when(employeeService.updateEmployee(nonExistingEmployee)).thenThrow(EmployeeNotFoundException.class);

        // Act
        ResponseEntity<?> responseEntity = employeeController.updateEmployee(nonExistingEmployee);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteEmployee() {
        // Arrange
        long id = 1L;
        when(employeeService.deleteEmployee(id)).thenReturn(true);

        // Act
        ResponseEntity<String> responseEntity = employeeController.deleteEmployee(id);

        // Assert
        assertEquals("Employee deleted successfully", responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
