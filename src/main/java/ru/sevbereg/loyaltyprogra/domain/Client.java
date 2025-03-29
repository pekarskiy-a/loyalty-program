package ru.sevbereg.loyaltyprogra.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "t_client")
public class Client extends AbstractPerson {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "lt_client_card",
            joinColumns = @JoinColumn(name = "c_client_id"),
            inverseJoinColumns = @JoinColumn(name = "c_card_id")
    )
    private Set<Card> cards;

    /**
     * Чёрный / белый списки и т.п.
     */
    @Column(name = "c_client_category")
    private String clientCategory;

    @Column(name = "c_comment")
    private String comment;
}
