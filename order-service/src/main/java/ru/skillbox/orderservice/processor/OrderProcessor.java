package ru.skillbox.orderservice.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;
import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.domain.OrderCreatedEvent;

@Component
public class OrderProcessor {

    private final Sinks.Many<OrderCreatedEvent> sink;

    @Autowired
    public OrderProcessor(Sinks.Many<OrderCreatedEvent> sink) {
        this.sink = sink;
    }

    public void process(Order order, Long userId) {
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(order.getId())
                .userId(userId)
                .cost(order.getCost())
                .products(order.getProducts())
                .build();

        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
