package com.poc.POC_CHAT.dtos;


import jakarta.validation.constraints.NotBlank;

public record ChatCreateRequestDTO(
        @NotBlank String content
) {}
