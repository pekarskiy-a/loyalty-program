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
import ru.sevbereg.loyaltyprogra.controller.api.EmployeeCreateRq;
import ru.sevbereg.loyaltyprogra.domain.Employee;
import ru.sevbereg.loyaltyprogra.facade.EmployeeFacade;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeFacade facade;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody EmployeeCreateRq request) {
        Employee employee;
        try {
            employee = facade.create(request);
        } catch (ValidationException | IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @GetMapping("/findBy/id/{id}")
    private ResponseEntity<?> findById(@PathVariable Long id) {
        Employee employee = facade.findById(id);
        if (Objects.isNull(employee)) {
            var message = String.format("Сотрудник с id: %s не найдена", id);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Employee request) {
        Employee employee;
        try {
            employee = facade.update(request);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            facade.deleteById(id);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        var message = String.format("Сотрудник с id: %s удалён", id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
