package ru.sevbereg.loyaltyprogra.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.sevbereg.loyaltyprogra.controller.api.TransactionCreateRq;
import ru.sevbereg.loyaltyprogra.domain.Transaction;
import ru.sevbereg.loyaltyprogra.util.IdempotencyKeyUtils;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "card", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "idempotencyKey", source = ".", qualifiedByName = "generateIk")
    Transaction mapToTransaction(TransactionCreateRq transactionCreateRq);

    /**
     * Ключ идемпотентности генерируется на основании информации о платеже и текущей даты
     *
     * @param request контейнер с информацией о транзакции
     * @return хэш на основании данных транзакции
     */
    @Named("generateIk")
    default String generateIk(TransactionCreateRq request) {
        final String rowString = String.format("%s:%s:%s:%s:%s:%s:%s",
                request.getEmployeeId(), request.getCardId(), request.getDescription(),
                request.getAmountSpent(), request.getBonusEarned(), request.getBonusSpent(), LocalDate.now());

        return IdempotencyKeyUtils.generateIdempotencyKey(rowString);
    }
}
