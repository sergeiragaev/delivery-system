package ru.skillbox.inventoryservice.domain.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skillbox.inventoryservice.domain.OrderProduct;
import ru.skillbox.inventoryservice.domain.enums.OrderStatus;

import java.util.List;

@ToString
@Setter
@Getter
@Builder
public class OrderPaidEvent implements Event {

    private static final String EVENT = "OrderPaid";

    private long orderId;
    private long userId;
    private OrderStatus status;
    private List<OrderProduct> products;
    private String destinationAddress;
    private String authHeaderValue;

    @Override
    public String getEvent() {
        return EVENT;
    }

}
