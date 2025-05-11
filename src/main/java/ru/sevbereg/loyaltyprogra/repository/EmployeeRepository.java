package ru.sevbereg.loyaltyprogra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sevbereg.loyaltyprogra.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByPhoneNumber(String phoneNumber);

    Employee findByBotState_TgUserId(Long tgUserId);

}
