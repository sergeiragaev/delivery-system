package ru.skillbox.inventoryservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.inventoryservice.domain.enums.OrderStatus;
import ru.skillbox.inventoryservice.domain.event.OrderDeliveredEvent;
import ru.skillbox.inventoryservice.domain.event.OrderInventedEvent;
import ru.skillbox.inventoryservice.service.InventoryService;


@Component
public class OrderDeliveredEventHandler implements EventHandler<OrderDeliveredEvent, OrderInventedEvent> {

    private final InventoryService inventoryService;
    private static final Logger logger = LoggerFactory.getLogger(OrderDeliveredEventHandler.class);

    @Autowired
    public OrderDeliveredEventHandler(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Transactional
    public OrderInventedEvent handleEvent(OrderDeliveredEvent event) {
        logger.info("Event handling: " + event);
        if (event.getStatus().equals(OrderStatus.DELIVERY_FAILED)) {
            OrderInventedEvent inventedEvent = OrderInventedEvent.builder()
                    .userId(event.getUserId())
                    .orderId(event.getOrderId())
                    .authHeaderValue(event.getAuthHeaderValue())
                    .status(OrderStatus.INVENTMENT_FAILED)
                    .products(event.getProducts())
                    .build();

            inventoryService.returnOrderToInvent(inventedEvent);

            return inventedEvent;
        } else {
            throw new RuntimeException();
        }
    }
}
