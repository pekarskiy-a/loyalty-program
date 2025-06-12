package ru.sevbereg.loyaltyprogra.facade.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sevbereg.loyaltyprogra.mapper.LoyaltyProgramMapper;
import ru.sevbereg.loyaltyprogra.service.LoyaltyProgramService;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class LoyaltyProgramFacadeImplTest {

    @Mock
    private LoyaltyProgramService service;

    @Mock
    private LoyaltyProgramMapper mapper;

    @InjectMocks
    private LoyaltyProgramFacadeImpl facade;

    @Test
    void justTest() {
        BigDecimal balanceChange = BigDecimal.valueOf(100L);

        balanceChange = balanceChange.add(BigDecimal.TEN);
        balanceChange = balanceChange.subtract(BigDecimal.ONE);

        System.out.println(balanceChange);
    }
}