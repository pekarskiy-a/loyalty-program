package ru.sevbereg.loyaltyprogra.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.sevbereg.loyaltyprogra.controller.api.EmployeeCreateRq;
import ru.sevbereg.loyaltyprogra.domain.Employee;
import ru.sevbereg.loyaltyprogra.util.PhoneFormatterUtils;

@Mapper(componentModel = "spring", imports = PhoneFormatterUtils.class)
public interface EmployeeMapper {

    @Mapping(target = "positions", ignore = true)
    @Mapping(target = "phoneNumber", expression = "java(PhoneFormatterUtils.normalizeRuPhone(employeeCreateRq.getPhoneNumber()))")
    Employee mapToEmployee(EmployeeCreateRq employeeCreateRq);

}
