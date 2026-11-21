package br.edu.atitus.gatewayapi.configs;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("products", route -> route.path("/products", "/products/**").uri("lb://product-api"))
                .route("product-images", route -> route.path("/images/**").uri("lb://product-api"))
                .route("product-management", route -> route.path("/ws/products/**").uri("lb://product-api"))
                .route("auth", route -> route.path("/auth/**").uri("lb://auth-api"))
                .route("currency", route -> route.path("/currencies/**").uri("lb://currency-api"))
                .route("orders", route -> route.path("/orders", "/orders/**").uri("lb://order-api"))
                .route("order-sales", route -> route.path("/ws/orders/**").uri("lb://order-api"))
                .build();
    }
}
