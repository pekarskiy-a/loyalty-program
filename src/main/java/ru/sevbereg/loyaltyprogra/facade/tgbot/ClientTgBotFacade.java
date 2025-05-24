package ru.sevbereg.loyaltyprogra.facade.tgbot;

import jakarta.validation.Valid;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sevbereg.loyaltyprogra.domain.Card;
import ru.sevbereg.loyaltyprogra.domain.Client;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyTier;
import ru.sevbereg.loyaltyprogra.domain.tgbot.UserBotState;
import ru.sevbereg.loyaltyprogra.enricher.ClientTemplateEnricher;
import ru.sevbereg.loyaltyprogra.service.CardService;
import ru.sevbereg.loyaltyprogra.service.ClientService;
import ru.sevbereg.loyaltyprogra.service.LoyaltyTierService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;
import ru.sevbereg.loyaltyprogra.tgbotapi.api.UpdateClientTemplate;
import ru.sevbereg.loyaltyprogra.util.JsonUtils;
import ru.sevbereg.loyaltyprogra.util.PhoneFormatterUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.zip.CRC32;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ClientTgBotFacade {

    private final ClientService clientService;

    private final LoyaltyTierService loyaltyTierService;

    private final ClientTemplateEnricher enricher;

    private final CardService cardService;

    private final UserBotStateService botStateService;

    @Value("${default.loyalty.tier.id}")
    private Long defaultLoyaltyTierId;

    public Client createTemplate(String phoneNumber, Long tgUserId) {
        log.info("Создание шаблона клиента");
        String formattedPhoneNumber = PhoneFormatterUtils.normalizeRuPhone(phoneNumber);
        LoyaltyTier loyaltyTier = loyaltyTierService.findById(defaultLoyaltyTierId);
        UserBotState currentUserBotState = botStateService.getUserBotStateByTgId(tgUserId);

        Card card = new Card();
        card.setLoyaltyTier(loyaltyTier);
        card.setAvailableBooking(loyaltyTier.isAvailableBooking());
        card.setCardNumber(this.generateCardNumber(formattedPhoneNumber));

        Set<Card> cards = new HashSet<>();
        cards.add(card);

        Client client = new Client();
        client.setPhoneNumber(formattedPhoneNumber);
        client.setCards(cards);
        client.setBotState(currentUserBotState);

        Client createdClient = clientService.create(client);
        log.info("Клиент создан");
        return createdClient;
    }

    public Client updateClientTemplate(@Valid UpdateClientTemplate clientDto) {
        log.info("Обновление шаблона клиента с tgUserId: {}", clientDto.getTgUserId());
        log.debug("Запрос на обновление клиента: {}{}", System.lineSeparator(), JsonUtils.toJson(clientDto));
        if (Objects.isNull(clientDto.getTgUserId())) {
            String message = "Не заполнен tgUserId";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        Client clientTemplate = clientService.findByTgUserId(clientDto.getTgUserId()); //todo если приходит null, то возвращается первый попавшийся, проверить почему так
        if (Objects.isNull(clientTemplate)) {
            String message = "Клиент не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        enricher.enrich(clientTemplate, clientDto);
        return clientTemplate;
    }

    public Client findByPhoneNumber(String phoneNumber) {
        log.info("Поиск клиента по phoneNumber: {}", phoneNumber);
        String formattedPhoneNumber = PhoneFormatterUtils.normalizeRuPhone(phoneNumber);
        return clientService.findByPhoneNumber(formattedPhoneNumber);
    }

    public Client findByTgUserId(Long tgUserId) {
        log.info("Поиск клиента по tgUserId: {}", tgUserId);
        return clientService.findByTgUserId(tgUserId);
    }

    public Client findByPhoneOrCardNumber(String request) {
        if (Objects.isNull(request)) {
            return null;
        }
        try {
            log.info("Поиск клиента по phoneNumber: {}", request);
            return this.findByPhoneNumber(request);
        } catch (IllegalArgumentException ex) {
            log.info("Номер не валиден, поиск клиента по cardNumber: {}", request);
            Card card = cardService.findByCardNumber(Long.parseLong(request));
            return Optional.ofNullable(card).map(Card::getClient).orElse(null);
        }
    }

    private long generateCardNumber(String phoneNumber) { // todo вынести в утилитку
        CRC32 crc = new CRC32();
        crc.update(phoneNumber.getBytes());
        return crc.getValue();
    }
}
