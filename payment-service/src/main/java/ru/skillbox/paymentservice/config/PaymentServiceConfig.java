package ru.skillbox.paymentservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.paymentservice.consumer.EventConsumer;
import ru.skillbox.paymentservice.domain.event.OrderCreatedEvent;
import ru.skillbox.paymentservice.domain.event.PaymentEvent;
import ru.skillbox.paymentservice.domain.event.TransactionEvent;
import ru.skillbox.paymentservice.handler.EventHandler;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class PaymentServiceConfig {
    private final EventHandler<OrderCreatedEvent, PaymentEvent> orderCreatedEventHandler;
    private final EventHandler<PaymentEvent, TransactionEvent> paymentEventHandler;
    private final EventConsumer<TransactionEvent> transactionEventConsumer;

    @Autowired
    public PaymentServiceConfig(
            EventHandler<OrderCreatedEvent, PaymentEvent> orderCreatedEventHandler,
            EventConsumer<TransactionEvent> transactionEventConsumer,
            EventHandler<PaymentEvent, TransactionEvent> paymentEventHandler) {
        this.orderCreatedEventHandler = orderCreatedEventHandler;
        this.transactionEventConsumer = transactionEventConsumer;
        this.paymentEventHandler = paymentEventHandler;
    }

    @Bean
    public Function<OrderCreatedEvent, PaymentEvent> orderEventProcessor() {
        return orderCreatedEventHandler::handleEvent;
    }
    @Bean
    public Function<PaymentEvent, TransactionEvent> paymentEventSubscriber() {
        return paymentEventHandler::handleEvent;
    }

    @Bean
    public Consumer<TransactionEvent> transactionEventSubscriber() {
        return transactionEventConsumer::consumeEvent;
    }

}
