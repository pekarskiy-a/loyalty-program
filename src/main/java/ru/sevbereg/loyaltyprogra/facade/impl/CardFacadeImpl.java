package ru.sevbereg.loyaltyprogra.facade.impl;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sevbereg.loyaltyprogra.domain.Card;
import ru.sevbereg.loyaltyprogra.domain.Employee;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyTier;
import ru.sevbereg.loyaltyprogra.domain.logging.EmployeeEventLog;
import ru.sevbereg.loyaltyprogra.domain.logging.EventTypeEnum;
import ru.sevbereg.loyaltyprogra.facade.CardFacade;
import ru.sevbereg.loyaltyprogra.service.CardService;
import ru.sevbereg.loyaltyprogra.service.EmployeeService;
import ru.sevbereg.loyaltyprogra.service.LoyaltyTierService;
import ru.sevbereg.loyaltyprogra.service.logging.EmployeeEventLogService;
import ru.sevbereg.loyaltyprogra.util.JsonUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CardFacadeImpl implements CardFacade {

    private final CardService cardService;
    private final LoyaltyTierService tierService;
    private final EmployeeService employeeService;
    private final EmployeeEventLogService logService;

    @Override
    public Card updateTierAndBalance(Long cardId, BigDecimal bonusEarned, BigDecimal bonusSpent) throws IllegalArgumentException {
        Card cardForUpdate = cardService.findById(cardId);

        if (Objects.isNull(cardForUpdate)) {
            throw new IllegalArgumentException("Карта не найдена. ID: " + cardId);
        }

        BigDecimal newBalance = this.getNewBalance(cardForUpdate.getBonusBalance(), bonusEarned, bonusSpent);
        LoyaltyTier newTier = this.getNewTier(cardForUpdate.getLoyaltyTier(), newBalance);

        if (Objects.nonNull(newTier)) {
            cardForUpdate.setLoyaltyTier(newTier);
        }
        cardForUpdate.setAvailableBooking(newTier.isAvailableBooking());
        cardForUpdate.setSumCancelledCheckIn(0);
        cardForUpdate.setBonusBalance(newBalance);
        return cardService.update(cardForUpdate);
    }

    @Override
    public Card addCancelledCheckIn(Long cardId, Long employeeTgId) {
        Card cardForUpdate = cardService.findById(cardId);

        if (Objects.isNull(cardForUpdate)) {
            throw new IllegalArgumentException("Карта не найдена. ID: " + cardId);
        }

        CardDto oldCardInfo = CardDto.builder()
                .cardId(cardId)
                .sumCancelledCheckIn(cardForUpdate.getSumCancelledCheckIn())
                .isAvailableBooking(cardForUpdate.isAvailableBooking())
                .build();

        int sumCancelledCheckIn = Integer.sum(cardForUpdate.getSumCancelledCheckIn(), 1);
        cardForUpdate.setSumCancelledCheckIn(sumCancelledCheckIn);
        if (sumCancelledCheckIn >= 2) {
            cardForUpdate.setAvailableBooking(false);
        }

        CardDto newCardInfo = CardDto.builder()
                .cardId(cardId)
                .sumCancelledCheckIn(cardForUpdate.getSumCancelledCheckIn())
                .isAvailableBooking(cardForUpdate.isAvailableBooking())
                .build();

        logService.logAction(this.buildEvent(JsonUtils.toJson(oldCardInfo), JsonUtils.toJson(newCardInfo), employeeTgId));
        return cardService.update(cardForUpdate);
    }

    private BigDecimal getNewBalance(BigDecimal currentBalance, BigDecimal bonusEarned, BigDecimal bonusSpent) {
        if (Objects.nonNull(bonusEarned)) {
            currentBalance = currentBalance.add(bonusEarned);
        }
        if (Objects.nonNull(bonusSpent)) {
            currentBalance = currentBalance.subtract(bonusSpent);
        }
        return currentBalance;
    }

    private LoyaltyTier getNewTier(LoyaltyTier currentTier, BigDecimal newBalance) {
        List<LoyaltyTier> activeTiers = tierService.findAllActiveOrderByNextValueAsc();
        LoyaltyTier newTier = currentTier;
        for (LoyaltyTier tier : activeTiers) {
            if (newBalance.compareTo(tier.getNextLevelValue()) > 0) {
                boolean isCurrentTierLess = currentTier.getNextLevelValue().compareTo(tier.getNextLevelValue()) < 0;
                if (isCurrentTierLess) {
                    newTier = tier;
                }
            } else {
                break;
            }
        }
        return newTier;
    }

    private EmployeeEventLog buildEvent(String oldValue, String newValue, Long employeeTgId) {
        Employee employee = employeeService.findByTgUserId(employeeTgId);

        EmployeeEventLog employeeEventLog = new EmployeeEventLog();
        employeeEventLog.setEventType(EventTypeEnum.UPDATE_CARD);
        employeeEventLog.setEventDescription("Отметка о не заезде");
        employeeEventLog.setOldValue(oldValue);
        employeeEventLog.setNewValue(newValue);
        employeeEventLog.setEmployee(employee);

        return employeeEventLog;
    }

    @Data
    @Builder
    static class CardDto {
        private long cardId;
        private int sumCancelledCheckIn;
        private boolean isAvailableBooking;
    }
}
