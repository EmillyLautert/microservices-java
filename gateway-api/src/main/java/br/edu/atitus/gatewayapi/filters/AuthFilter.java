package br.edu.atitus.gatewayapi.filters;

import br.edu.atitus.gatewayapi.components.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.List;
import java.nio.charset.StandardCharsets;

@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private static final List<String> IDENTITY_HEADERS = List.of("X-User-Id", "X-User-Type", "X-User-Email");
    private final JwtUtil jwtUtil;
    public AuthFilter(JwtUtil jwtUtil) { this.jwtUtil = jwtUtil; }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest sanitized = exchange.getRequest().mutate()
                .headers(headers -> IDENTITY_HEADERS.forEach(headers::remove)).build();
        String path = exchange.getRequest().getURI().getPath();
        if (!isProtected(path)) return chain.filter(exchange.mutate().request(sanitized).build());

        String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer ")) return reject(exchange, HttpStatus.UNAUTHORIZED);
        Claims claims = jwtUtil.validateToken(authorization.substring(7));
        if (claims == null) return reject(exchange, HttpStatus.UNAUTHORIZED);

        Object rawId = claims.get("id");
        Object rawType = claims.get("type");
        String email = claims.get("email", String.class);
        if (!(rawId instanceof Number id) || !(rawType instanceof Number type) || email == null)
            return reject(exchange, HttpStatus.UNAUTHORIZED);

        boolean sellerRoute = path.startsWith("/ws/");
        int userType = type.intValue();
        if ((sellerRoute && userType != 0) || (!sellerRoute && userType != 1))
            return reject(exchange, HttpStatus.FORBIDDEN);

        ServerHttpRequest authenticated = sanitized.mutate().headers(headers -> {
            headers.set("X-User-Id", id.toString());
            headers.set("X-User-Type", Integer.toString(userType));
            headers.set("X-User-Email", email);
        }).build();
        return chain.filter(exchange.mutate().request(authenticated).build());
    }

    private boolean isProtected(String path) {
        return path.equals("/orders") || path.startsWith("/orders/") || path.equals("/ws") || path.startsWith("/ws/");
    }

    private Mono<Void> reject(ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        String message = status == HttpStatus.UNAUTHORIZED
                ? "Token ausente ou inválido"
                : "Usuário sem permissão para esta operação";
        var body = exchange.getResponse().bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(body));
    }

    @Override public int getOrder() { return -1; }
}
