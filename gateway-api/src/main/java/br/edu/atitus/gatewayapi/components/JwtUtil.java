package br.edu.atitus.gatewayapi.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {
    private final SecretKey signingKey;
    public JwtUtil(@Value("${app.jwt.secret}") String secret) {
        byte[] key = secret.getBytes(StandardCharsets.UTF_8);
        if (key.length < 32) throw new IllegalArgumentException("JWT_SECRET precisa ter ao menos 32 bytes");
        signingKey = Keys.hmacShaKeyFor(key);
    }
    public Claims validateToken(String token) {
        try { return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload(); }
        catch (Exception ignored) { return null; }
    }
}
