package com.swapnil.service.impl;

import com.swapnil.exception.EmployeeNotFoundException;
import com.swapnil.model.Employee;
import com.swapnil.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void testGetAllEmployees() {
        // Arrange
        List<Employee> mockEmployees = List.of(
                new Employee(1, "John", "Doe", "john@example.com", "Manager"),
                new Employee(2,  "Jane", "Doe", "jane@example.com", "Developer")
        );
        when(employeeRepository.findAll()).thenReturn(mockEmployees);

        // Act
        List<Employee> result = employeeService.getAllEmployees();

        // Assert
        assertEquals(mockEmployees, result);
    }

    @Test
    void testGetEmployeeById() {
        // Arrange
        long id = 1L;
        Employee mockEmployee = new Employee(id, "John", "Doe", "john@example.com", "Manager");
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));

        // Act
        Optional<Employee> result = employeeService.getEmployeeById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(mockEmployee, result.get());
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        // Arrange
        long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(id));
    }

    @Test
    void testAddEmployee() {
        // Arrange
        Employee mockEmployee = new Employee(1, "John", "Doe", "john@example.com", "Manager");
        when(employeeRepository.save(mockEmployee)).thenReturn(mockEmployee);

        // Act
        Employee result = employeeService.addEmployee(mockEmployee);

        // Assert
        assertEquals(mockEmployee, result);
    }

    @Test
    void testUpdateEmployee() {
        // Arrange
        Employee existingEmployee = new Employee(1, "John", "Doe", "john@example.com", "Manager");
        when(employeeRepository.existsById(existingEmployee.getEmployeeId())).thenReturn(true);
        when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);

        // Act
        Optional<Employee> result = employeeService.updateEmployee(existingEmployee);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(existingEmployee, result.get());
    }

    @Test
    void testUpdateEmployeeNotFound() {
        // Arrange
        Employee nonExistingEmployee = new Employee(1, "John", "Doe", "john@example.com", "Manager");
        when(employeeRepository.existsById(nonExistingEmployee.getEmployeeId())).thenReturn(false);

        // Act and Assert
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(nonExistingEmployee));
    }

    @Test
    void testDeleteEmployee() {
        // Arrange
        long id = 1L;
        when(employeeRepository.existsById(id)).thenReturn(true);

        // Act
        boolean result = employeeService.deleteEmployee(id);

        // Assert
        assertTrue(result);
        verify(employeeRepository, times(1)).deleteById(id);
    }
    @Test
    void testDeleteEmployeeNotFound() {
    	// Arrange
        long nonExistentId = 99L;
        when(employeeRepository.existsById(nonExistentId)).thenReturn(false);

        // Act and Assert
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(nonExistentId));
    }

}
