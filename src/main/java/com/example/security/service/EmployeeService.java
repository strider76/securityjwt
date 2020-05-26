package com.example.security.service;

import com.example.security.model.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> GetAll();
    Employee Create(Employee employee);

}
