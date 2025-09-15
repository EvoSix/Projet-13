package com.poc.POC_CHAT.dtos;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String lastname,
        String email,
        String adresse,
        String role,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {}
