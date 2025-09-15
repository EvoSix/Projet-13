package com.poc.POC_CHAT.controllers;



import com.poc.POC_CHAT.dtos.AuthResponseDTO;
import com.poc.POC_CHAT.dtos.RegisterRequestDTO;
import com.poc.POC_CHAT.dtos.UserRequestDTO;
import com.poc.POC_CHAT.interfaces.IAuthService;
import com.poc.POC_CHAT.securities.JwtService;
import com.poc.POC_CHAT.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuthService auth;

    public AuthController(IAuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(auth.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(auth.register(request));
    }
}
