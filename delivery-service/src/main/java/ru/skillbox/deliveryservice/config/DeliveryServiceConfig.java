package ru.skillbox.deliveryservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.deliveryservice.domain.event.OrderInventedEvent;
import ru.skillbox.deliveryservice.domain.event.OrderDeliveredEvent;
import ru.skillbox.deliveryservice.handler.EventHandler;

import java.util.function.Function;

@Configuration
public class DeliveryServiceConfig {

    private final EventHandler<OrderInventedEvent, OrderDeliveredEvent> orderInventedEventHandler;

    @Autowired
    public DeliveryServiceConfig(
            EventHandler<OrderInventedEvent, OrderDeliveredEvent> orderInventedEventHandler) {
        this.orderInventedEventHandler = orderInventedEventHandler;
    }

    @Bean
    public Function<OrderInventedEvent, OrderDeliveredEvent> orderEventProcessor() {
        return orderInventedEventHandler::handleEvent;
    }

}
