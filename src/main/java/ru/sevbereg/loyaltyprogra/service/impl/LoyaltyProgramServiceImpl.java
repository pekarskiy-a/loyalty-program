package ru.sevbereg.loyaltyprogra.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyProgram;
import ru.sevbereg.loyaltyprogra.repository.LoyaltyProgramRepository;
import ru.sevbereg.loyaltyprogra.service.LoyaltyProgramService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoyaltyProgramServiceImpl implements LoyaltyProgramService {

    private final LoyaltyProgramRepository repository;

    @Override
    public List<LoyaltyProgram> findAll() {
        return repository.findAll();
    }

    @Override
    public LoyaltyProgram findById(String id) {
        return repository.findById(id);
    }

    @Override
    public LoyaltyProgram save(LoyaltyProgram entity) {
        return repository.save(entity);
    }

    @Override
    public LoyaltyProgram update(LoyaltyProgram entity) {
        return repository.save(entity);
    }

    @Override
    public LoyaltyProgram softDelete(LoyaltyProgram entity) {
        entity.setActive(false);
        return repository.save(entity);
    }

    @Override
    public void delete(LoyaltyProgram entity) {
        repository.delete(entity);
    }
}
