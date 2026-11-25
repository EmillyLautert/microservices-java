package br.edu.atitus.authapi.entities;

import br.edu.atitus.authapi.components.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTypeTest {
    @Test
    void roleCodesAreExplicitAndStable() {
        assertEquals(0, UserType.Admin.getCode());
        assertEquals(1, UserType.Common.getCode());
    }

    @Test
    void rolePersistenceUsesNamesInsteadOfEnumPositions() throws Exception {
        Enumerated mapping = UserEntity.class.getDeclaredField("type").getAnnotation(Enumerated.class);

        assertEquals(EnumType.STRING, mapping.value());
    }

    @Test
    void generatedJwtKeepsTheStableNumericRoleCode() {
        String secret = "let-tera-stable-test-secret-32-bytes";
        UserEntity user = new UserEntity();
        user.setId(12L);
        user.setEmail("seller@example.com");
        user.setType(UserType.Admin);
        String token = new JwtUtil(secret).generateToken(user);

        var claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build().parseSignedClaims(token).getPayload();

        assertEquals(0, ((Number) claims.get("type")).intValue());
    }
}
