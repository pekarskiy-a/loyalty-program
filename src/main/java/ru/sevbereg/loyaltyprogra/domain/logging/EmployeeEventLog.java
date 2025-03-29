package ru.sevbereg.loyaltyprogra.domain.logging;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.sevbereg.loyaltyprogra.domain.AbstractIdentifiableEntity;
import ru.sevbereg.loyaltyprogra.domain.Employee;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "t_employee_event_log")
public class EmployeeEventLog extends AbstractIdentifiableEntity {

    @Column(name = "c_rq_uid", nullable = false, unique = true)
    private UUID rqUid;

    @Column(name = "c_create_date")
    private Instant createDate;

    @Column(name = "c_event_type")
    private String eventType;

    @Column(name = "c_event_description")
    private String eventDescription;

    @Column(columnDefinition = "JSON", name = "c_old_value")
    private String oldValue;

    @Column(columnDefinition = "JSON", name = "c_new_value")
    private String newValue;

    @ManyToOne
    @JoinColumn(name = "c_employee_id")
    private Employee employee;
}
