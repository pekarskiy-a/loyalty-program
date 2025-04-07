package ru.sevbereg.loyaltyprogra.facade;

import ru.sevbereg.loyaltyprogra.controller.api.EmployeeCreateRq;
import ru.sevbereg.loyaltyprogra.domain.Employee;

public interface EmployeeFacade {

    Employee create(EmployeeCreateRq request);

    Employee findById(Long id);

    Employee update(Employee request);

    void deleteById(Long id);

}
