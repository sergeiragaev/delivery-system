package ru.skillbox.orderservice.processor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;
import ru.skillbox.orderservice.domain.model.Order;
import ru.skillbox.orderservice.domain.event.OrderCreatedEvent;

@Component
public class OrderProcessor {

    private final Sinks.Many<OrderCreatedEvent> sink;

    @Autowired
    public OrderProcessor(Sinks.Many<OrderCreatedEvent> sink) {
        this.sink = sink;
    }

    public void process(Order order, HttpServletRequest request) {
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(order.getId())
                .userId(Long.parseLong(request.getHeader("id")))
                .cost(order.getCost())
                .products(order.getProducts())
                .destinationAddress(order.getDestinationAddress())
                .authHeaderValue(request.getHeader("Authorization"))
                .build();

        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
