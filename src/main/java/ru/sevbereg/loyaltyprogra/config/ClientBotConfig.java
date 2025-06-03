package ru.sevbereg.loyaltyprogra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.sevbereg.loyaltyprogra.facade.tgbot.EmployeeTgBotFacade;
import ru.sevbereg.loyaltyprogra.tgbotapi.LoyaltyProgramBot;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.ClientTgBotHandler;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.EmployeeTgBotHandler;

@Configuration
public class ClientBotConfig {

    @Bean
    public MessageSource messageSource() {
        var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages_for_client");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(LoyaltyProgramBot loyaltyProgramBot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(loyaltyProgramBot);
        return botsApi;
    }

    @Bean
    public LoyaltyProgramBot loyaltyProgramBot(
            @Value("${telegram.bot.token}") String token,
            @Value("${telegram.bot.username}") String username,
            EmployeeTgBotFacade employeeFacade,
            ClientTgBotHandler clientHandler,
            EmployeeTgBotHandler employeeHandler
    ) {
        return new LoyaltyProgramBot(token, username, employeeFacade, clientHandler, employeeHandler);
    }

}
