package com.poc.POC_CHAT.dtos;

public record AuthResponseDTO(
        String token,
        UserResponseDTO user
) {}
