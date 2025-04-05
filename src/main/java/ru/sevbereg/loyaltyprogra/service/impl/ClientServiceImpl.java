package ru.sevbereg.loyaltyprogra.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sevbereg.loyaltyprogra.domain.Client;
import ru.sevbereg.loyaltyprogra.repository.ClientRepository;
import ru.sevbereg.loyaltyprogra.service.ClientService;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    @Override
    public Client create(Client entity) {
        entity.addCards(entity.getCards());
        return repository.save(entity);
    }

    @Override
    public Client update(Client entity) {
        entity.addCards(entity.getCards());
        return repository.save(entity);
    }

    @Override
    public Client findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Client findByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public void delete(Client entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
