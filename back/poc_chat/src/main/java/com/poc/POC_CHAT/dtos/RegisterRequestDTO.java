package com.poc.POC_CHAT.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record RegisterRequestDTO(
        @NotBlank String firstname,
        @NotBlank String lastname,
        @Email @NotBlank String email,
        @NotBlank String adresse,
        @Size(min = 6) @NotBlank String password,
        String role // optionnel, sinon "USER"
) {}
