package ru.sevbereg.loyaltyprogra.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AbstractMutableEntity extends AbstractIdentifiableEntity {

    @Version
    @Column(name = "c_entity_version")
    private Long version;

    @Column(name = "c_update_date")
    private Instant updateDate;

    @Column(name = "c_is_active")
    private boolean isActive = true;

    @PreUpdate
    private void prePersist() {
        updateDate = Instant.now();
    }

}
