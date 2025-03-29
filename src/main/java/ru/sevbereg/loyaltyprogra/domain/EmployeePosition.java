package ru.sevbereg.loyaltyprogra.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "t_employee_position")
public class EmployeePosition extends AbstractIdentifiableEntity {

    @Column(name = "t_position_name")
    private String positionName;

}
