package ru.sevbereg.loyaltyprogra.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "t_employee_position")
@EqualsAndHashCode(callSuper = true)
public class EmployeePosition extends AbstractIdentifiableEntity {

    @Column(name = "t_position_name", unique = true, nullable = false)
    private String positionName;

    @Transient
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Employee> employee;

}
