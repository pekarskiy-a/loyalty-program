package ru.sevbereg.loyaltyprogra.facade;

import ru.sevbereg.loyaltyprogra.controller.api.ClientCreateRq;
import ru.sevbereg.loyaltyprogra.domain.Client;

public interface ClientFacade {

    /**
     * Метод создания клиента и карты. По дефолту у одного клиента создается одна карта.
     * В методе валидируется номер клиента и преобразуется к общему виду и проверяется наличие в БД клиента с таким номером.
     *
     * @param request
     * @return
     * @throws IllegalArgumentException, {@link jakarta.validation.ValidationException}
     */
    Client create(ClientCreateRq request);

    Client update(Client updateRq);

    Client findById(String id);

    Client findByPhoneNumber(String phoneNumber);

    void deleteById(Long id);

}
