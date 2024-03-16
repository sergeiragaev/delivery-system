package ru.skillbox.paymentservice.domain.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ru.skillbox.paymentservice.domain.enums.OrderStatus;

@ToString
@Getter
@Builder
public class OrderInventEvent implements Event {

    private static final String EVENT = "OrderInvent";
    private long orderId;
    private long userId;
    private OrderStatus status;
    @ToString.Exclude
    private String authHeaderValue;

    @Override
    public String getEvent() {
        return EVENT;
    }

}
