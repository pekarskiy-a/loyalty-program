package ru.sevbereg.loyaltyprogra.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "t_employee")
@EqualsAndHashCode(callSuper = true)
public class Employee extends AbstractPerson {

    /**
     * Должность
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "lt_employee_positions",
            joinColumns = @JoinColumn(name = "c_employee_id"),
            inverseJoinColumns = @JoinColumn(name = "c_position_id")
    )
    private Set<EmployeePosition> positions;

    @Column(name = "c_comment", length = 1000)
    private String comment;

}
