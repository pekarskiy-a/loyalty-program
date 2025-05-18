package ru.sevbereg.loyaltyprogra.domain.logging;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.sevbereg.loyaltyprogra.domain.AbstractIdentifiableEntity;
import ru.sevbereg.loyaltyprogra.domain.Employee;

import java.util.UUID;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "t_employee_event_log")
@EqualsAndHashCode(callSuper = true)
public class EmployeeEventLog extends AbstractIdentifiableEntity {

    @Column(name = "c_rq_uid", unique = true)
    private UUID rqUid;

    @Column(name = "c_event_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private EventTypeEnum eventType;

    @Column(name = "c_event_description")
    private String eventDescription;

    @Column(columnDefinition = "JSON", name = "c_old_value")
    private String oldValue;

    @Column(columnDefinition = "JSON", name = "c_new_value")
    private String newValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_employee_id")
    private Employee employee;
}
