package ru.skillbox.deliveryservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.deliveryservice.domain.ServiceName;
import ru.skillbox.deliveryservice.domain.dto.StatusDto;
import ru.skillbox.deliveryservice.domain.enums.OrderStatus;
import ru.skillbox.deliveryservice.domain.event.InventoryEvent;
import ru.skillbox.deliveryservice.domain.event.DeliveryEvent;
import ru.skillbox.deliveryservice.service.DeliveryService;
import ru.skillbox.deliveryservice.service.RequestSendingService;

import static ru.skillbox.deliveryservice.domain.enums.OrderStatus.INVENTED;


@Component
public class InventoryEventHandler implements EventHandler<InventoryEvent, DeliveryEvent> {

    private final DeliveryService deliveryService;
    private final RequestSendingService requestSendingService;
    private static final Logger logger = LoggerFactory.getLogger(InventoryEventHandler.class);

    @Autowired
    public InventoryEventHandler(DeliveryService deliveryService, RequestSendingService requestSendingService) {
        this.deliveryService = deliveryService;
        this.requestSendingService = requestSendingService;
    }

    @Transactional
    public DeliveryEvent handleEvent(InventoryEvent event) {
        logger.info("Event handling: " + event);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (INVENTED.equals(event.getStatus())) {
            DeliveryEvent deliveredEvent = DeliveryEvent.builder()
                    .userId(event.getUserId())
                    .orderId(event.getOrderId())
                    .authHeaderValue(event.getAuthHeaderValue())
                    .destinationAddress(event.getDestinationAddress())
                    .status(OrderStatus.DELIVERED)
                    .products(event.getProducts())
                    .build();

            String comment = deliveryService.deliverOrder(deliveredEvent);

            StatusDto statusDto = new StatusDto();
            statusDto.setStatus(deliveredEvent.getStatus());
            statusDto.setServiceName(ServiceName.DELIVERY_SERVICE);
            statusDto.setComment(comment);
            requestSendingService.updateOrderStatusInOrderService(event.getOrderId(), statusDto, event.getAuthHeaderValue());

            return deliveredEvent;
        }
        return null;
    }
}
