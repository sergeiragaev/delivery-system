package ru.skillbox.inventoryservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.inventoryservice.domain.event.OrderInventedEvent;
import ru.skillbox.inventoryservice.domain.event.OrderPaidEvent;
import ru.skillbox.inventoryservice.handler.EventHandler;

import java.util.function.Function;

@Configuration
public class InventoryServiceConfig {

    private final EventHandler<OrderPaidEvent, OrderInventedEvent> orderPaidEventHandler;

    @Autowired
    public InventoryServiceConfig(
            EventHandler<OrderPaidEvent, OrderInventedEvent> orderPaidEventHandler) {
        this.orderPaidEventHandler = orderPaidEventHandler;
    }

    @Bean
    public Function<OrderPaidEvent, OrderInventedEvent> orderEventProcessor() {
        return orderPaidEventHandler::handleEvent;
    }

}
