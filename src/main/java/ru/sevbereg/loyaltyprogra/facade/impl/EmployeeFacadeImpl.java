package ru.sevbereg.loyaltyprogra.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sevbereg.loyaltyprogra.controller.api.EmployeeCreateRq;
import ru.sevbereg.loyaltyprogra.domain.Employee;
import ru.sevbereg.loyaltyprogra.domain.EmployeePosition;
import ru.sevbereg.loyaltyprogra.facade.EmployeeFacade;
import ru.sevbereg.loyaltyprogra.mapper.EmployeeMapper;
import ru.sevbereg.loyaltyprogra.service.EmployeePositionService;
import ru.sevbereg.loyaltyprogra.service.EmployeeService;
import ru.sevbereg.loyaltyprogra.util.PhoneFormatterUtils;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeFacadeImpl implements EmployeeFacade {

    private final EmployeeService employeeService;

    private final EmployeeMapper employeeMapper;

    private final EmployeePositionService positionService;

    @Override
    @Transactional
    public Employee create(EmployeeCreateRq request) {
        UUID rqUid = UUID.randomUUID();
        String formattedPhoneNumber = PhoneFormatterUtils.normalizeRuPhone(request.getPhoneNumber());

        Employee employeeByPhoneNumber = employeeService.findByPhoneNumber(formattedPhoneNumber);
        if (Objects.nonNull(employeeByPhoneNumber)) {
            String message = String.format("[rqUid %s] Сотрудник с номером: %s уже существует", rqUid, request.getPhoneNumber());
            throw new IllegalArgumentException(message);
        }

        Set<EmployeePosition> employeePositions = request.getPositionIds().stream()
                .map(positionService::findById)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());

        if (employeePositions.isEmpty()) {
            throw new IllegalArgumentException("У сотрудника должна быть заполнена хотя бы одна роль.");
        }

        Employee employee = employeeMapper.mapToEmployee(request);
        employee.setPositions(employeePositions);

        return employeeService.save(employee);
    }

    @Override
    @Transactional
    public Employee findById(Long id) {
        return employeeService.findById(id);
    }

    @Override
    @Transactional
    public Employee update(Employee request) {
        return employeeService.update(request);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Employee employee = employeeService.findById(id);

        if (Objects.isNull(employee)) {
            return;
        }
        employeeService.delete(employee);
    }
}
