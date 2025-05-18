package ru.sevbereg.loyaltyprogra.repository.logging;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sevbereg.loyaltyprogra.domain.logging.EmployeeEventLog;

public interface EmployeeEventLogRepository extends JpaRepository<EmployeeEventLog, Long> {
}
