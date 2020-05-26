package com.example.security.service.impl;

import com.example.security.dao.EmployeeRepository;
import com.example.security.model.Employee;
import com.example.security.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    @Override
    public List<Employee> GetAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee Create(Employee employee) {

        return employeeRepository.save(employee);

    }
}
