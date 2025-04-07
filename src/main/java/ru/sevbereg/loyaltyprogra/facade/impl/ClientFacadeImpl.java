package ru.sevbereg.loyaltyprogra.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sevbereg.loyaltyprogra.controller.api.CardCreateRq;
import ru.sevbereg.loyaltyprogra.controller.api.ClientCreateRq;
import ru.sevbereg.loyaltyprogra.domain.Card;
import ru.sevbereg.loyaltyprogra.domain.Client;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyTier;
import ru.sevbereg.loyaltyprogra.facade.ClientFacade;
import ru.sevbereg.loyaltyprogra.mapper.ClientMapper;
import ru.sevbereg.loyaltyprogra.service.ClientService;
import ru.sevbereg.loyaltyprogra.service.LoyaltyTierService;
import ru.sevbereg.loyaltyprogra.util.PhoneFormatterUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.CRC32;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientFacadeImpl implements ClientFacade {

    private final ClientService clientService;

    private final LoyaltyTierService loyaltyTierService;

    private final ClientMapper clientMapper;

    @Override
    @Transactional
    public Client create(ClientCreateRq request) {
        UUID rqUid = UUID.randomUUID();
        if (Objects.isNull(request.getCards()) || request.getCards().size() != 1) {
            throw new IllegalArgumentException("[rqUid {}] У нового клиента должна быть одна карта");
        }
        CardCreateRq cardCreateRq = request.getCards().stream().findFirst().get();

        String formattedPhoneNumber = PhoneFormatterUtils.normalizeRuPhone(request.getPhoneNumber());

        Client clientByPhoneNumber = clientService.findByPhoneNumber(formattedPhoneNumber);
        if (Objects.nonNull(clientByPhoneNumber)) {
            String message = String.format("[rqUid %s] Клиент с номером: %s уже существует", rqUid, request.getPhoneNumber());
            throw new IllegalArgumentException(message);
        }

        LoyaltyTier loyaltyTier = Optional.ofNullable(loyaltyTierService.findById(cardCreateRq.getLoyaltyTierId()))
                .orElseThrow(() -> new IllegalArgumentException("Отсутствует уровень программы лояльности c id" + cardCreateRq.getLoyaltyTierId()));

        Client client = clientMapper.mapToClient(request);

        Card card = client.getCards().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Данные карты должны быть заполнены"));
        card.setLoyaltyTier(loyaltyTier);
        card.setAvailableBooking(loyaltyTier.isAvailableBooking());

        if (Objects.isNull(card.getCardNumber())) {
            card.setCardNumber(this.generateCardNumber(formattedPhoneNumber));
        }

        return clientService.create(client);
    }

    @Override
    @Transactional
    public Client update(Client updateRq) {
        return clientService.update(updateRq);
    }

    @Override
    @Transactional
    public Client findById(String id) {
        return clientService.findById(id);
    }

    @Override
    @Transactional
    public Client findByPhoneNumber(String phoneNumber) {
        return clientService.findByPhoneNumber(phoneNumber);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Client client = clientService.findById(id.toString());
        if (Objects.isNull(client)) {
            return;
        }
        clientService.delete(client);
    }

    private long generateCardNumber(String phoneNumber) {
        CRC32 crc = new CRC32();
        crc.update(phoneNumber.getBytes());
        return crc.getValue();
    }
}
