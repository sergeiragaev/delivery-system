package ru.skillbox.inventoryservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.inventoryservice.consumer.EventConsumer;
import ru.skillbox.inventoryservice.domain.event.InventoryEvent;
import ru.skillbox.inventoryservice.domain.event.PaymentEvent;
import ru.skillbox.inventoryservice.domain.event.TransactionEvent;
import ru.skillbox.inventoryservice.handler.EventHandler;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class InventoryServiceConfig {

    private final EventHandler<PaymentEvent, InventoryEvent> orderPaidEventHandler;
    private final EventHandler<InventoryEvent, TransactionEvent> inventoryEventHandler;
    private final EventConsumer<TransactionEvent> transactionEventConsumer;

    @Autowired
    public InventoryServiceConfig(
            EventHandler<PaymentEvent, InventoryEvent> orderPaidEventHandler,
            EventHandler<InventoryEvent, TransactionEvent> inventoryEventHandler,
            EventConsumer<TransactionEvent> transactionEventConsumer) {
        this.orderPaidEventHandler = orderPaidEventHandler;
        this.inventoryEventHandler = inventoryEventHandler;
        this.transactionEventConsumer = transactionEventConsumer;
    }

    @Bean
    public Function<PaymentEvent, InventoryEvent> orderPaidEventProcessor() {
        return orderPaidEventHandler::handleEvent;
    }

    @Bean
    public Function<InventoryEvent, TransactionEvent> inventoryEventSubscriber() {
        return inventoryEventHandler::handleEvent;
    }

    @Bean
    public Consumer<TransactionEvent> transactionEventSubscriber() {
        return transactionEventConsumer::consumeEvent;
    }

}
