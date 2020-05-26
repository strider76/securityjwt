package com.example.security.mapper;

import com.example.security.dto.EmployeeDTO;
import com.example.security.model.Employee;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface EmployeeMapper {


    @Mapping(target = "nombre", source = "name")
    EmployeeDTO daoToDto(Employee employee);

    List<EmployeeDTO> daoListToDtoList(List<Employee> employees);

    @InheritInverseConfiguration
    Employee dtoToDao(EmployeeDTO employeeDTO);

}
