package ru.sevbereg.loyaltyprogra.facade;

import ru.sevbereg.loyaltyprogra.controller.api.LoyaltyProgramCreateRq;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyProgram;

public interface LoyaltyProgramFacade {

    /**
     * Метод создания программы и условий
     *
     * @param request запрос с данными о программе и условий
     * @return созданная программа лояльности
     */
    LoyaltyProgram create(LoyaltyProgramCreateRq request);

    /**
     * Метод поиска программы и условий
     *
     * @param id id программы
     * @return обновленная программа лояльности
     */
    LoyaltyProgram findById(String id);

    /**
     * Метод обновления программы и условий
     *
     * @param request запрос с данными о программе и условий
     * @return обновленная программа лояльности
     */
    LoyaltyProgram update(LoyaltyProgram request);

    /**
     * Метод удаления программы и условий по id программы
     *
     * @param id id программы
     */
    void deleteById(Long id);

}
