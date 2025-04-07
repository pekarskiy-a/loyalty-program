package ru.sevbereg.loyaltyprogra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sevbereg.loyaltyprogra.domain.EmployeePosition;

public interface EmployeePositionRepository extends JpaRepository<EmployeePosition, Long> {

}
