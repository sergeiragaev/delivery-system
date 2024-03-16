package ru.skillbox.inventoryservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skillbox.inventoryservice.domain.enums.OrderStatus;
import ru.skillbox.inventoryservice.domain.event.OrderInventedEvent;
import ru.skillbox.inventoryservice.domain.event.OrderPaidEvent;
import ru.skillbox.inventoryservice.service.InventoryService;


@Component
public class OrderPaidEventHandler implements EventHandler<OrderPaidEvent, OrderInventedEvent> {

    private final InventoryService inventoryService;
    private static final Logger logger = LoggerFactory.getLogger(OrderPaidEventHandler.class);

    @Autowired
    public OrderPaidEventHandler(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public OrderInventedEvent handleEvent(OrderPaidEvent event) {
        logger.info("Event handling: " + event);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        OrderInventedEvent inventedEvent = OrderInventedEvent.builder()
                .userId(event.getUserId())
                .orderId(event.getOrderId())
                .authHeaderValue(event.getAuthHeaderValue())
                .status(OrderStatus.INVENTED)
                .products(event.getProducts())
                .build();

        inventoryService.inventOrder(inventedEvent);

        return inventedEvent;
    }
}
