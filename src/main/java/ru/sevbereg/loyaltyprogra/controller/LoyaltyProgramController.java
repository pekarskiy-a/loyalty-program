package ru.sevbereg.loyaltyprogra.controller;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sevbereg.loyaltyprogra.controller.api.LoyaltyProgramCreateRq;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyProgram;
import ru.sevbereg.loyaltyprogra.facade.LoyaltyProgramFacade;

import java.util.Objects;

@RestController
@RequestMapping("/api/loyaltyprogram")
@RequiredArgsConstructor
public class LoyaltyProgramController {

    private final LoyaltyProgramFacade facade;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody LoyaltyProgramCreateRq request) {
        LoyaltyProgram loyaltyProgram;
        try {
            loyaltyProgram = facade.create(request);
        } catch (ValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(loyaltyProgram, HttpStatus.CREATED);
    }

    @GetMapping("/findBy/id/{id}")
    private ResponseEntity<?> findById(@PathVariable String id) {
        LoyaltyProgram loyaltyProgram = facade.findById(id);
        if (Objects.isNull(loyaltyProgram)) {
            var message = String.format("Программа лояльности с id: %s не найдена", id);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return new ResponseEntity<>(loyaltyProgram, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody LoyaltyProgram request) {
        LoyaltyProgram updatedProgram;
        try {
            updatedProgram = facade.update(request);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(updatedProgram, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            facade.deleteById(id);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        var successMessage = String.format("Продукт с id: %s удалён", id);
        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }

}
