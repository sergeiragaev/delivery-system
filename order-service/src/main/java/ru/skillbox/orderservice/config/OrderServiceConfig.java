package ru.skillbox.orderservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import ru.skillbox.orderservice.consumer.EventConsumer;
import ru.skillbox.orderservice.domain.event.OrderCreatedEvent;
import ru.skillbox.orderservice.domain.event.TransactionEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class OrderServiceConfig {

    private final EventConsumer<TransactionEvent> transactionEventConsumer;

    @Autowired
    public OrderServiceConfig(EventConsumer<TransactionEvent> transactionEventConsumer) {
        this.transactionEventConsumer = transactionEventConsumer;
    }

    @Bean
    public Sinks.Many<OrderCreatedEvent> sink() {
        return Sinks.many()
                .multicast()
                .directBestEffort();
    }
    @Bean
    public Supplier<Flux<OrderCreatedEvent>> orderCreatedEventPublisher(
            Sinks.Many<OrderCreatedEvent> publisher) {
        return publisher::asFlux;
    }

    @Bean
    public Consumer<TransactionEvent> transactionEventProcessor() {
        return transactionEventConsumer::consumeEvent;
    }

}
