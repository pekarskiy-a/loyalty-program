package ru.sevbereg.loyaltyprogra.facade;

import ru.sevbereg.loyaltyprogra.controller.api.TransactionCreateRq;
import ru.sevbereg.loyaltyprogra.domain.Transaction;

public interface TransactionFacade {

    Transaction createAndSendTgMessage(TransactionCreateRq request);


}
