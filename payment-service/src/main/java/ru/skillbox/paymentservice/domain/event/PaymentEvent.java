package ru.skillbox.paymentservice.domain.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skillbox.paymentservice.domain.OrderProduct;
import ru.skillbox.paymentservice.domain.enums.OrderStatus;

import java.util.List;

@ToString
@Setter
@Getter
@Builder
public class PaymentEvent implements Event {

    private static final String EVENT = "OrderPaid";

    private long orderId;
    private long userId;
    private long cost;
    private OrderStatus status;
    private List<OrderProduct> products;
    private String destinationAddress;
    @ToString.Exclude
    private String authHeaderValue;

    @Override
    public String getEvent() {
        return EVENT;
    }

}
