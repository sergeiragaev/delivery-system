package ru.skillbox.paymentservice.domain.event;

import lombok.Data;
import lombok.ToString;
import ru.skillbox.paymentservice.domain.OrderProduct;

import java.util.List;

@Data
@ToString
public class OrderCreatedEvent implements Event {

    private static final String EVENT = "OrderCreated";

    private long orderId;
    private long userId;
    private long cost;
    private List<OrderProduct> products;
    private String destinationAddress;
    @ToString.Exclude
    private String authHeaderValue;

    @Override
    public String getEvent() {
        return EVENT;
    }
}
