package ru.sevbereg.loyaltyprogra.facade.tgbot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sevbereg.loyaltyprogra.domain.Employee;
import ru.sevbereg.loyaltyprogra.service.EmployeeService;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeTgBotFacade {

    private final EmployeeService employeeService;

    public Employee findByTgUserId(Long tgUserId) { //todo проверить что возвращает null или exception
        return employeeService.findByTgUserId(tgUserId);
    }

}
