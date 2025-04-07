package ru.sevbereg.loyaltyprogra.service;

import ru.sevbereg.loyaltyprogra.domain.Employee;

import java.util.List;

public interface EmployeeService {

    Employee save(Employee employee);

    Employee findById(Long id);

    Employee update(Employee employee);

    void delete(Employee employee);

    List<Employee> findAll();

    Employee findByPhoneNumber(String phoneNumber);
}
