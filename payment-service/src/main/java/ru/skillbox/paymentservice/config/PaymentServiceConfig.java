package ru.skillbox.paymentservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.paymentservice.consumer.EventConsumer;
import ru.skillbox.paymentservice.domain.event.OrderCreatedEvent;
import ru.skillbox.paymentservice.domain.event.OrderPaidEvent;
import ru.skillbox.paymentservice.domain.event.OrderInventEvent;
import ru.skillbox.paymentservice.handler.EventHandler;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class PaymentServiceConfig {
    private final EventHandler<OrderCreatedEvent, OrderPaidEvent> orderCreatedEventHandler;
    private final EventConsumer<OrderInventEvent> inventoryEventConsumer;

    @Autowired
    public PaymentServiceConfig(
            EventHandler<OrderCreatedEvent, OrderPaidEvent> orderCreatedEventHandler,
            EventConsumer<OrderInventEvent> inventoryEventConsumer) {
        this.orderCreatedEventHandler = orderCreatedEventHandler;
        this.inventoryEventConsumer = inventoryEventConsumer;
    }

    @Bean
    public Function<OrderCreatedEvent, OrderPaidEvent> orderEventProcessor() {
        return orderCreatedEventHandler::handleEvent;
    }
    @Bean
    public Consumer<OrderInventEvent> inventoryEventProcessor() {
        return inventoryEventConsumer::consumeEvent;
    }

}
