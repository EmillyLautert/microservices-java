package br.edu.atitus.authapi.components;

import br.edu.atitus.authapi.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private static final long EXPIRATION_MILLIS = 24L * 60 * 60 * 1000;
    private final SecretKey signingKey;

    public JwtUtil(@Value("${app.jwt.secret}") String secret) {
        byte[] key = secret.getBytes(StandardCharsets.UTF_8);
        if (key.length < 32) throw new IllegalArgumentException("JWT_SECRET precisa ter ao menos 32 bytes");
        this.signingKey = Keys.hmacShaKeyFor(key);
    }

    public String generateToken(UserEntity user) {
        Date now = new Date();
        return Jwts.builder().claim("id", user.getId()).claim("email", user.getEmail())
                .claim("type", user.getType().getCode()).issuedAt(now)
                .expiration(new Date(now.getTime() + EXPIRATION_MILLIS))
                .signWith(signingKey, Jwts.SIG.HS256).compact();
    }

    public Claims validateToken(String token) {
        try { return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload(); }
        catch (Exception ignored) { return null; }
    }
}
