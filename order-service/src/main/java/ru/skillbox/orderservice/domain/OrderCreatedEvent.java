package ru.skillbox.orderservice.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderCreatedEvent implements Event {

    private static final String EVENT = "OrderCreated";

    private Long orderId;
    private Long userId;
    private Long cost;
    private List<OrderProduct> products;

    @Override
    public String getEvent() {
        return EVENT;
    }
}
