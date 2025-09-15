package com.poc.POC_CHAT.services;

import com.poc.POC_CHAT.dtos.*;
import com.poc.POC_CHAT.interfaces.IAuthService;
import com.poc.POC_CHAT.models.user;
import com.poc.POC_CHAT.repositories.UserRepository;
import com.poc.POC_CHAT.securities.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;


@Service
public class AuthService implements IAuthService {
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwt;
    private final UserRepository users;

    public AuthService(AuthenticationManager authManager,
                           PasswordEncoder passwordEncoder,
                           JwtService jwt,
                           UserRepository users) {
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
        this.jwt = jwt;
        this.users = users;
    }


    public AuthResponseDTO login(UserRequestDTO request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        ); // → DaoAuthenticationProvider + UserDetailsService + PasswordEncoder :contentReference[oaicite:2]{index=2}

        String subject = auth.getName(); // généralement l'email/username
        String token = jwt.generateToken(subject); // JJWT 0.12+ via ton JwtService :contentReference[oaicite:3]{index=3}

        user u = users.findByEmail(subject)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur introuvable"));
        return new AuthResponseDTO(token, toResponse(u));
    }


    public AuthResponseDTO register(RegisterRequestDTO request) {

        if (users.findByEmail(request.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email déjà utilisé"); // simple et idiomatique :contentReference[oaicite:4]{index=4}
        }

        user entity = new user();
        entity.setFirstname(request.firstname());
        entity.setLastname(request.lastname());
        entity.setEmail(request.email());
        entity.setAdresse(request.adresse());
        entity.setRole(request.role() == null || request.role().isBlank() ? "USER" : request.role().toUpperCase());
        entity.setPassword(passwordEncoder.encode(request.password()));
        entity.setCreated_at(LocalDateTime.now());
        entity.setUpdated_at(LocalDateTime.now());

        user saved = users.save(entity);

        // Génère un token immédiatement après inscription (pratique pour auto-login)
        String token = jwt.generateToken(saved.getEmail()); // JJWT 0.12+ :contentReference[oaicite:5]{index=5}
        return new AuthResponseDTO(token, toResponse(saved));
    }

    private UserResponseDTO toResponse(user e) {
        return new UserResponseDTO(
                e.getId(),
                e.getLastname(),
                e.getEmail(),
                e.getAdresse(),
                e.getRole(),
                e.getCreated_at(),
                e.getUpdated_at()
        );
    }
}
