package ru.skillbox.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.gateway.security.AuthenticationFilter;

@Configuration
public class GatewayConfig {

    private final AuthenticationFilter filter;

    @Autowired
    public GatewayConfig(AuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(
                        "order_route", r -> r.path("/api/order/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://ORDER-SERVICE")
                )
                .route(
                        "auth_route", r -> r.path("/auth/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://AUTH-SERVICE")
                )
                .route(
                        "payment_route", r -> r.path("/api/payment/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://PAYMENT-SERVICE")
                )
                .route(
                        "inventory_route", r -> r.path("/api/inventory/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://INVENTORY-SERVICE")
                )
                .route(
                        "delivery_route", r -> r.path("/api/delivery/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://DELIVERY-SERVICE")
                )
                .build();
    }
}
