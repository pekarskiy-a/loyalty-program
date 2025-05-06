package ru.sevbereg.loyaltyprogra.facade.tgbot;

import jakarta.validation.Valid;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sevbereg.loyaltyprogra.domain.Card;
import ru.sevbereg.loyaltyprogra.domain.Client;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyTier;
import ru.sevbereg.loyaltyprogra.domain.tgbot.ClientBotState;
import ru.sevbereg.loyaltyprogra.enricher.ClientTemplateEnricher;
import ru.sevbereg.loyaltyprogra.service.ClientService;
import ru.sevbereg.loyaltyprogra.service.LoyaltyTierService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;
import ru.sevbereg.loyaltyprogra.tgbotapi.api.UpdateClientTemplate;
import ru.sevbereg.loyaltyprogra.util.PhoneFormatterUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.zip.CRC32;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientTgBotFacade {

    private final ClientService clientService;

    private final LoyaltyTierService loyaltyTierService;

    private final ClientTemplateEnricher enricher;

    protected final UserBotStateService botStateService;

    @Value("${default.loyalty.tier.id}")
    private Long defaultLoyaltyTierId;

    public Client createTemplate(String phoneNumber, Long tgUserId) {
        String formattedPhoneNumber = PhoneFormatterUtils.normalizeRuPhone(phoneNumber);
        LoyaltyTier loyaltyTier = loyaltyTierService.findById(defaultLoyaltyTierId);
        ClientBotState currentClientBotState = botStateService.getUserBotStateByTgId(tgUserId);

        Card card = new Card();
        card.setLoyaltyTier(loyaltyTier);
        card.setAvailableBooking(loyaltyTier.isAvailableBooking());
        card.setCardNumber(this.generateCardNumber(formattedPhoneNumber));

        Set<Card> cards = new HashSet<>();
        cards.add(card);

        Client client = new Client();
        client.setPhoneNumber(formattedPhoneNumber);
        client.setCards(cards);
        client.setBotState(currentClientBotState);

        return clientService.create(client);
    }

    public Client updateClientTemplate(@Valid UpdateClientTemplate clientDto) {
        if (Objects.isNull(clientDto.getTgUserId())) {
            throw new IllegalArgumentException("Не заполнен TgUserId");
        }
        Client clientTemplate = clientService.findByTgUserId(clientDto.getTgUserId()); //todo если приходит null, то возвращается первый попавшийся, проверить почему так
        if (Objects.isNull(clientTemplate)) {
            throw new NotFoundException("Клиент не найден");
        }
        enricher.enrich(clientTemplate, clientDto);
        return clientTemplate;
    }

    public Client findByPhoneNumber(String phoneNumber) {
        String formattedPhoneNumber = PhoneFormatterUtils.normalizeRuPhone(phoneNumber);
        return clientService.findByPhoneNumber(formattedPhoneNumber);
    }

    public Client findByTgUserId(Long tgUserId) {
        return clientService.findByTgUserId(tgUserId);
    }

    private long generateCardNumber(String phoneNumber) { // todo вынести в утилитку
        CRC32 crc = new CRC32();
        crc.update(phoneNumber.getBytes());
        return crc.getValue();
    }
}
