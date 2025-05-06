package ru.sevbereg.loyaltyprogra.domain.tgbot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.sevbereg.loyaltyprogra.domain.AbstractIdentifiableEntity;
import ru.sevbereg.loyaltyprogra.domain.Client;

@Entity
@Table(name = "t_user_bot_state")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClientBotState extends AbstractIdentifiableEntity {

    public ClientBotState(Long tgUserId, BotState botState) {
        this.tgUserId = tgUserId;
        this.botState = botState;
    }

    @Column(name = "c_tg_user_id", nullable = false, unique = true)
    private Long tgUserId;

    @Column(name = "c_bot_state", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private BotState botState;

    @OneToOne
    @JoinColumn(name = "c_client_id")
    private Client pers;

}
