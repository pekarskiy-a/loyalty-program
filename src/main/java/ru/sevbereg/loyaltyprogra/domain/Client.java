package ru.sevbereg.loyaltyprogra.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.sevbereg.loyaltyprogra.domain.tgbot.ClientBotState;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "t_client")
@EqualsAndHashCode(callSuper = true)
public class Client extends AbstractPerson {

    @JsonIgnoreProperties("client")
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Card> cards = new HashSet<>();

    /**
     * Чёрный / белый списки и т.п.
     */
    @Column(name = "c_client_category")
    private String clientCategory;

    @Column(name = "c_comment", length = 1000)
    private String comment;

    @OneToOne
    @JoinColumn(name = "c_tg_user_id", referencedColumnName = "c_tg_user_id")
    private ClientBotState botState;

    public void addCard(Card card) {
        cards.add(card);
        card.setClient(this);
    }

    public Client addCards(Set<Card> cards) {
        cards.forEach(this::addCard);
        return this;
    }
}
