package ru.skillbox.orderservice.domain.event;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import ru.skillbox.orderservice.domain.model.OrderProduct;

import java.util.List;

@Data
@Builder
@ToString
public class OrderCreatedEvent implements Event {

    private static final String EVENT = "OrderCreated";

    private Long orderId;
    private Long userId;
    private Long cost;
    private List<OrderProduct> products;
    private String destinationAddress;
    @ToString.Exclude
    private String authHeaderValue;

    @Override
    public String getEvent() {
        return EVENT;
    }
}
