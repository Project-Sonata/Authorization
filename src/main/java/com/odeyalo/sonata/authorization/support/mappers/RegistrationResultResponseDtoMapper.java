package com.odeyalo.sonata.authorization.support.mappers;

import com.odeyalo.sonata.authorization.dto.RegistrationResultResponseDto;
import com.odeyalo.sonata.authorization.service.registration.RegistrationResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegistrationResultResponseDtoMapper {

    RegistrationResultResponseDto from(RegistrationResult registrationResult);

}
