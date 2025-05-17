package ru.sevbereg.loyaltyprogra.controller;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sevbereg.loyaltyprogra.controller.api.TransactionCreateRq;
import ru.sevbereg.loyaltyprogra.domain.Transaction;
import ru.sevbereg.loyaltyprogra.facade.TransactionFacade;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionFacade facade;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TransactionCreateRq request) {
        Transaction transaction;
        try {
            transaction = facade.createAndSendTgMessage(request);
        } catch (ValidationException | IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }
}
