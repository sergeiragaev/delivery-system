package ru.skillbox.gateway.security;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class RouterValidator {

    public static final List<Pattern> openEndpoints = List.of(
            Pattern.compile("/auth/user/signup"),
            Pattern.compile("/auth/token/generate"),
            Pattern.compile("/auth/actuator/prometheus"),
            Pattern.compile("/auth/v3/api-docs.*"),
            Pattern.compile("/api/order/v3/api-docs.*"),
            Pattern.compile("/api/payment/v3/api-docs.*"),
            Pattern.compile("/api/inventory/v3/api-docs.*"),
            Pattern.compile("/api/delivery/v3/api-docs.*"),
            Pattern.compile("/actuator/prometheus"),
            Pattern.compile("/api/order/actuator/prometheus"),
            Pattern.compile("/api/payment/actuator/prometheus"),
            Pattern.compile("/api/inventory/actuator/prometheus"),
            Pattern.compile("/api/delivery/actuator/prometheus")
    );

    public static final Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints
                    .stream()
                    .noneMatch(pattern -> pattern.matcher(request.getURI().getPath()).matches());

}
