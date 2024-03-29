package ru.skillbox.deliveryservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.deliveryservice.domain.event.InventoryEvent;
import ru.skillbox.deliveryservice.domain.event.DeliveryEvent;
import ru.skillbox.deliveryservice.domain.event.TransactionEvent;
import ru.skillbox.deliveryservice.handler.EventHandler;

import java.util.function.Function;

@Configuration
public class DeliveryServiceConfig {

    private final EventHandler<InventoryEvent, DeliveryEvent> inventoryEventHandler;
    private final EventHandler<DeliveryEvent, TransactionEvent> delivereryEventHandler;

    @Autowired
    public DeliveryServiceConfig(
            EventHandler<InventoryEvent, DeliveryEvent> inventoryEventHandler, EventHandler<DeliveryEvent, TransactionEvent> delivereryEventHandler) {
        this.inventoryEventHandler = inventoryEventHandler;
        this.delivereryEventHandler = delivereryEventHandler;
    }

    @Bean
    public Function<InventoryEvent, DeliveryEvent> inventoryEventProcessor() {
        return inventoryEventHandler::handleEvent;
    }

    @Bean
    public Function<DeliveryEvent, TransactionEvent> transactionEventSubscriber() {
        return delivereryEventHandler::handleEvent;
    }

}
