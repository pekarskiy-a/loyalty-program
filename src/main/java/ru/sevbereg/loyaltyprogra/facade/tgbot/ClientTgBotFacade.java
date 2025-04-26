package ru.sevbereg.loyaltyprogra.facade.tgbot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sevbereg.loyaltyprogra.domain.Card;
import ru.sevbereg.loyaltyprogra.domain.Client;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyTier;
import ru.sevbereg.loyaltyprogra.enricher.ClientTemplateEnricher;
import ru.sevbereg.loyaltyprogra.service.ClientService;
import ru.sevbereg.loyaltyprogra.service.LoyaltyTierService;
import ru.sevbereg.loyaltyprogra.tgbotapi.api.UpdateClientTemplate;
import ru.sevbereg.loyaltyprogra.util.PhoneFormatterUtils;

import java.util.Set;
import java.util.zip.CRC32;

@Service
@RequiredArgsConstructor
public class ClientTgBotFacade {

    private final ClientService clientService;

    private final LoyaltyTierService loyaltyTierService;

    private final ClientTemplateEnricher enricher;

    @Value("default.loyalty.tier.id")
    private Long defaultLoyaltyTierId;

    @Transactional
    public Client createTemplate(String phoneNumber) {
        String formattedPhoneNumber = PhoneFormatterUtils.normalizeRuPhone(phoneNumber);
        LoyaltyTier loyaltyTier = loyaltyTierService.findById(defaultLoyaltyTierId);

        Card card = new Card();
        card.setLoyaltyTier(loyaltyTier);
        card.setAvailableBooking(loyaltyTier.isAvailableBooking());
        card.setCardNumber(this.generateCardNumber(formattedPhoneNumber));

        Client client = new Client();
        client.setPhoneNumber(formattedPhoneNumber);
        client.setCards(Set.of(card));

        return clientService.create(client);
    }

    public Client updateClientTemplate(UpdateClientTemplate clientDto) {
        Client clientTemplate = clientService.findByTgUserId(clientDto.getTgUserId());
        enricher.enrich(clientTemplate, clientDto);
        return clientTemplate;
    }

    public Client findByPhoneNumber(String phoneNumber) {
        String formattedPhoneNumber = PhoneFormatterUtils.normalizeRuPhone(phoneNumber);
        return clientService.findByPhoneNumber(formattedPhoneNumber);
    }

    private long generateCardNumber(String phoneNumber) { // todo вынести в утилитку
        CRC32 crc = new CRC32();
        crc.update(phoneNumber.getBytes());
        return crc.getValue();
    }
}
