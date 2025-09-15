package com.poc.POC_CHAT.securities;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.Instant;
import java.util.function.Function;

@Component
public class JwtService {
    private final SecretKey key;
    private final long ttlSeconds;

    public JwtService(
            @Value("${jwt.secret}") String base64Secret,
            @Value("${jwt.expiration-ms:3600}") long ttlSeconds) {
        // secret attendu en Base64 : on le décode puis on construit la clé HMAC
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
        this.ttlSeconds = ttlSeconds;
    }

    public String generateToken(String subject) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(ttlSeconds)))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, String expectedSubject) {
        try {
            Claims claims = parse(token);
            boolean notExpired = claims.getExpiration().toInstant().isAfter(Instant.now());
            boolean subjectOk = expectedSubject == null || expectedSubject.equals(claims.getSubject());
            return notExpired && subjectOk;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    private Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)           // nouvelle API 0.12+ : vérification de signature
                .build()
                .parseSignedClaims(token)  // lève JwtException si invalide/expiré/signature KO
                .getPayload();
    }
    public <T> T extractClaim(String token, Function<Claims, T> extractor) {
        return extractor.apply(parse(token));
    }
}
