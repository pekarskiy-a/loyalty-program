package ru.sevbereg.loyaltyprogra.service.logging;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sevbereg.loyaltyprogra.domain.logging.EmployeeEventLog;
import ru.sevbereg.loyaltyprogra.repository.logging.EmployeeEventLogRepository;

@Service
@RequiredArgsConstructor
public class EmployeeEventLogService {

    private final EmployeeEventLogRepository repository;

    public void logAction(EmployeeEventLog action) {
        repository.save(action);
    }
}
