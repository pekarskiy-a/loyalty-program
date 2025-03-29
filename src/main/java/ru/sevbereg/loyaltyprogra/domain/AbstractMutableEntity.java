package ru.sevbereg.loyaltyprogra.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AbstractMutableEntity extends AbstractIdentifiableEntity {

    @Version
    @Column(name = "c_entity_version")
    private Long version;

//    /**
//     * Уникальный id сущности (заполняется при сохранении объекта в БД)
//     */
//    @Column(name = "c_sbid")
//    private String sbId;

    @Column(name = "c_creat_date")
    private Instant creatDate;

    @Column(name = "c_update_date")
    private Instant updateDate;

    @Column(name = "c_is_active")
    private boolean isActive = true;

    @PrePersist
    private void prePersist() {
        if (Objects.isNull(updateDate)) {
            updateDate = Instant.now();
        }
    }

}
