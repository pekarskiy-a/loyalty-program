package ru.sevbereg.loyaltyprogra.domain.tgbot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.sevbereg.loyaltyprogra.domain.AbstractIdentifiableEntity;

@Entity
@Table(name = "t_user_bot_state")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserBotState extends AbstractIdentifiableEntity {

    public UserBotState(Long tgUserId, Long tgChatId, BotState botState, Role role) {
        this.tgUserId = tgUserId;
        this.tgChatId = tgChatId;
        this.botState = botState;
        this.role = role;
    }

    @Column(name = "c_tg_user_id", nullable = false, unique = true)
    private Long tgUserId;

    @Column(name = "c_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "c_tg_chat_id")
    private Long tgChatId;

    @Column(name = "c_bot_state", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private BotState botState;

    /**
     * Служебное поле для сохранения id карты клиента подлежащего обновлению
     */
    @Column(name = "c_update_card_id")
    private Long updateCardId;

}
