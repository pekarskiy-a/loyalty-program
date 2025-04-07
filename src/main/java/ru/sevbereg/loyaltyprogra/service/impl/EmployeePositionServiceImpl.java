package ru.sevbereg.loyaltyprogra.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sevbereg.loyaltyprogra.domain.EmployeePosition;
import ru.sevbereg.loyaltyprogra.repository.EmployeePositionRepository;
import ru.sevbereg.loyaltyprogra.service.EmployeePositionService;

@Service
@RequiredArgsConstructor
public class EmployeePositionServiceImpl implements EmployeePositionService {

    private final EmployeePositionRepository repository;

    @Override
    public EmployeePosition findById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
