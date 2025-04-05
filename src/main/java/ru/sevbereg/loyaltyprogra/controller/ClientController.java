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
import ru.sevbereg.loyaltyprogra.controller.api.ClientCreateRq;
import ru.sevbereg.loyaltyprogra.domain.Client;
import ru.sevbereg.loyaltyprogra.facade.ClientFacade;

import java.util.Objects;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientFacade facade;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ClientCreateRq request) {
        Client client;
        try {
            client = facade.create(request);
        } catch (ValidationException | IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }

    @GetMapping("/findBy/id/{id}")
    private ResponseEntity<?> findById(@PathVariable String id) {
        Client client = facade.findById(id);
        if (Objects.isNull(client)) {
            var message = String.format("Клиент с id: %s не найдена", id);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Client request) {
        Client client;
        try {
            client = facade.update(request);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            facade.deleteById(id);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        var message = String.format("Клиент с id: %s удалён", id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
