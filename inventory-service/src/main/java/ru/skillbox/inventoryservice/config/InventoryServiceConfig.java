package ru.skillbox.inventoryservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.inventoryservice.domain.event.OrderDeliveredEvent;
import ru.skillbox.inventoryservice.domain.event.OrderInventedEvent;
import ru.skillbox.inventoryservice.domain.event.OrderPaidEvent;
import ru.skillbox.inventoryservice.handler.EventHandler;

import java.util.function.Function;

@Configuration
public class InventoryServiceConfig {

    private final EventHandler<OrderPaidEvent, OrderInventedEvent> orderPaidEventHandler;
    private final EventHandler<OrderDeliveredEvent, OrderInventedEvent> orderDeliveredEventHandler;

    @Autowired
    public InventoryServiceConfig(
            EventHandler<OrderPaidEvent, OrderInventedEvent> orderPaidEventHandler,
            EventHandler<OrderDeliveredEvent, OrderInventedEvent> orderDeliveredEventHandler) {
        this.orderPaidEventHandler = orderPaidEventHandler;
        this.orderDeliveredEventHandler = orderDeliveredEventHandler;
    }

    @Bean
    public Function<OrderPaidEvent, OrderInventedEvent> orderPaidEventProcessor() {
        return orderPaidEventHandler::handleEvent;
    }

    @Bean
    public Function<OrderDeliveredEvent, OrderInventedEvent> orderDeliveredEventProcessor() {
        return orderDeliveredEventHandler::handleEvent;
    }

}
