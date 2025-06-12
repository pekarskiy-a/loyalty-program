package ru.sevbereg.loyaltyprogra.domain.logging;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON", name = "c_old_value")
    private String oldValue;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON", name = "c_new_value")
    private String newValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_employee_id")
    private Employee employee;
}
