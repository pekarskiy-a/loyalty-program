package ru.sevbereg.loyaltyprogra.facade.impl;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sevbereg.loyaltyprogra.controller.api.LoyaltyProgramCreateRq;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyProgram;
import ru.sevbereg.loyaltyprogra.facade.LoyaltyProgramFacade;
import ru.sevbereg.loyaltyprogra.mapper.LoyaltyProgramMapper;
import ru.sevbereg.loyaltyprogra.service.LoyaltyProgramService;
import ru.sevbereg.loyaltyprogra.service.LoyaltyTierService;
import ru.sevbereg.loyaltyprogra.util.JsonUtils;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoyaltyProgramFacadeImpl implements LoyaltyProgramFacade {

    private final LoyaltyProgramMapper lpMapper;

    private final LoyaltyProgramService lpService;

    private final LoyaltyTierService tierService;

    @Override
    @Transactional
    public LoyaltyProgram create(LoyaltyProgramCreateRq request) {
        var rqUid = UUID.randomUUID();
        this.nameExistCheck(request.getLpName(), rqUid);
        log.info("[rqUid {}] Формирование запроса на создание программы лояльности.", rqUid);
        LoyaltyProgram loyaltyProgramDbRq = lpMapper.mapToLoyaltyProgram(request);

        log.debug("[rqUid {}] Запрос сформирован: {}{}", rqUid, System.lineSeparator(), JsonUtils.toJson(loyaltyProgramDbRq));

        LoyaltyProgram savedLp = lpService.save(loyaltyProgramDbRq);
        log.info("[rqUid {}] Новая программа лояльности успешно сохранена.", rqUid);
        log.debug("[rqUid {}] Запрос сформирован: {}{}", rqUid, System.lineSeparator(), JsonUtils.toJson(savedLp));
        return savedLp;
    }

    @Override
    @Transactional
    public LoyaltyProgram findById(String id) {
        var rqUid = UUID.randomUUID();
        log.info("[rqUid {}] Поиск программы лояльности по id - {}.", rqUid, id);
        return lpService.findById(id);
    }

    @Override
    @Transactional
    public LoyaltyProgram update(LoyaltyProgram request) {
        return lpService.update(request);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        LoyaltyProgram loyaltyProgram = lpService.findById(id.toString());
        if (Objects.isNull(loyaltyProgram)) {
            return;
        }
        lpService.delete(loyaltyProgram);
    }

    private void nameExistCheck(String lpName, UUID rqUid) {
        if (Objects.nonNull(lpService.findByLpName(lpName))) {
            String message = String.format("[rqUid %s] Программа лояльности с именем %s уже существует.", rqUid, lpName);
            log.info(message);
            throw new ValidationException(message);
        }
    }

}
