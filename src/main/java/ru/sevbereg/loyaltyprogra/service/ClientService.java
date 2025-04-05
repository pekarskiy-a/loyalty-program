package ru.sevbereg.loyaltyprogra.service;

import ru.sevbereg.loyaltyprogra.domain.Client;

public interface ClientService {

    Client create(Client entity);

    Client update(Client entity);

    Client findById(String id);

    Client findByPhoneNumber(String phoneNumber);

    void delete(Client entity);

    void deleteById(Long id);

}
