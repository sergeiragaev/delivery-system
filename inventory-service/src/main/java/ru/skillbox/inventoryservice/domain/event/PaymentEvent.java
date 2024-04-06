package ru.skillbox.inventoryservice.domain.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skillbox.inventoryservice.domain.enums.OrderStatus;
import ru.skillbox.inventoryservice.domain.model.InventsProduct;

import java.util.List;

@ToString
@Setter
@Getter
@Builder
public class PaymentEvent implements Event {

    private static final String EVENT = "OrderPaid";

    private long orderId;
    private long userId;
    private OrderStatus status;
    private List<InventsProduct> products;
    private String destinationAddress;
    private String authHeaderValue;

    @Override
    public String getEvent() {
        return EVENT;
    }

}
