package com.poc.POC_CHAT.mappers;

import com.poc.POC_CHAT.dtos.UserRequestDTO;
import com.poc.POC_CHAT.dtos.UserResponseDTO;
import com.poc.POC_CHAT.models.user;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface UserMapper {

    // Entity → ResponseDTO (sans mot de passe)
    @Mapping(target = "created_at", source = "created_at")
    @Mapping(target = "updated_at", source = "updated_at")
    UserResponseDTO toResponseDto(user entity);

    // RequestDTO → Entity (construit l’entity depuis le login & password)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    user toEntity(UserRequestDTO dto);

    // Eviter de perdre les anciens champs lors des mises à jour
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    void updateEntityFromRequest(UserRequestDTO dto, @MappingTarget user entity);
}
