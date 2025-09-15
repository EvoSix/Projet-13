package com.poc.POC_CHAT.dtos;

import java.time.LocalDateTime;

public record ChatDTO(
        Long id,
        String content,
        String authorFirstname,
        String authorLastname,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {}