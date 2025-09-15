package com.poc.POC_CHAT.dtos;

public record UserRequestDTO(
        String email,
        String password
) {}