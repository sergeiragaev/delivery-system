package ru.skillbox.deliveryservice.domain.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skillbox.deliveryservice.domain.OrderProduct;
import ru.skillbox.deliveryservice.domain.enums.OrderStatus;

import java.util.List;

@ToString
@Setter
@Getter
@Builder
public class InventoryEvent implements Event {

    private static final String EVENT = "OrderInvented";

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
