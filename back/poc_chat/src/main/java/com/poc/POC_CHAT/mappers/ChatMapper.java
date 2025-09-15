package com.poc.POC_CHAT.mappers;

import com.poc.POC_CHAT.dtos.ChatDTO;
import com.poc.POC_CHAT.models.chat;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(target = "created_at", source = "created_at")
    @Mapping(target = "updated_at", source = "updated_at")
    ChatDTO toDto(chat entity);

    @Mapping(target = "created_at", source = "created_at")
    @Mapping(target = "updated_at", source = "updated_at")
    chat toEntity(ChatDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "created_at", source = "created_at")
    @Mapping(target = "updated_at", source = "updated_at")
    void updateEntity(ChatDTO dto, @org.mapstruct.MappingTarget chat entity);
}
