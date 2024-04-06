package ru.skillbox.deliveryservice.handler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.deliveryservice.domain.event.DeliveryEvent;
import ru.skillbox.deliveryservice.domain.event.TransactionEvent;

import static ru.skillbox.deliveryservice.domain.enums.OrderStatus.DELIVERED;
import static ru.skillbox.deliveryservice.domain.enums.TransactionStatus.SUCCESSFUL;
import static ru.skillbox.deliveryservice.domain.enums.TransactionStatus.UNSUCCESSFUL;


@Component
public class DeliveryEventHandler implements EventHandler<DeliveryEvent, TransactionEvent> {

    @Transactional
    public TransactionEvent handleEvent(DeliveryEvent event) {

        return TransactionEvent.builder()
                .orderId(event.getOrderId())
                .userId(event.getUserId())
                .status(DELIVERED.equals(event.getStatus()) ? SUCCESSFUL : UNSUCCESSFUL)
                .build();
    }

}
