package ru.skillbox.inventoryservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skillbox.inventoryservice.domain.ServiceName;
import ru.skillbox.inventoryservice.domain.dto.StatusDto;
import ru.skillbox.inventoryservice.domain.enums.OrderStatus;
import ru.skillbox.inventoryservice.domain.event.InventoryEvent;
import ru.skillbox.inventoryservice.domain.event.PaymentEvent;
import ru.skillbox.inventoryservice.service.InventoryService;
import ru.skillbox.inventoryservice.service.RequestSendingService;

import static ru.skillbox.inventoryservice.domain.enums.OrderStatus.PAID;


@Component
public class PaymentEventHandler implements EventHandler<PaymentEvent, InventoryEvent> {

    private final InventoryService inventoryService;
    private final RequestSendingService requestSendingService;
    private static final Logger logger = LoggerFactory.getLogger(PaymentEventHandler.class);

    @Autowired
    public PaymentEventHandler(InventoryService inventoryService, RequestSendingService requestSendingService) {
        this.inventoryService = inventoryService;
        this.requestSendingService = requestSendingService;
    }

    public InventoryEvent handleEvent(PaymentEvent event) {
        logger.info("Event handling: " + event);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (PAID.equals(event.getStatus())) {
            InventoryEvent inventedEvent = InventoryEvent.builder()
                    .userId(event.getUserId())
                    .orderId(event.getOrderId())
                    .authHeaderValue(event.getAuthHeaderValue())
                    .destinationAddress(event.getDestinationAddress())
                    .status(OrderStatus.INVENTED)
                    .products(event.getProducts())
                    .build();

            String comment = inventoryService.inventOrder(inventedEvent);

            StatusDto statusDto = new StatusDto();
            statusDto.setServiceName(ServiceName.INVENTORY_SERVICE);
            statusDto.setStatus(inventedEvent.getStatus());
            statusDto.setComment(comment);
            requestSendingService.updateOrderStatusInOrderService(event.getOrderId(), statusDto, event.getAuthHeaderValue());

            return inventedEvent;
        }

        return null;
    }
}
