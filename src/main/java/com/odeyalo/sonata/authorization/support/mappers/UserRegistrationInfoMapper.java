package com.odeyalo.sonata.authorization.support.mappers;

import com.odeyalo.sonata.authorization.service.registration.RegistrationForm;
import com.odeyalo.sonata.common.authentication.dto.request.UserRegistrationInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRegistrationInfoMapper {

    @Mapping(target = "email", source = "username")
    UserRegistrationInfo fromRegistrationForm(RegistrationForm form);

}
