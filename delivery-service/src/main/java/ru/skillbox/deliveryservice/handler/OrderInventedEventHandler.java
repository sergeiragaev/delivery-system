package ru.skillbox.deliveryservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.deliveryservice.domain.ServiceName;
import ru.skillbox.deliveryservice.domain.dto.StatusDto;
import ru.skillbox.deliveryservice.domain.enums.OrderStatus;
import ru.skillbox.deliveryservice.domain.event.OrderInventedEvent;
import ru.skillbox.deliveryservice.domain.event.OrderDeliveredEvent;
import ru.skillbox.deliveryservice.exception.NothingToDoException;
import ru.skillbox.deliveryservice.service.DeliveryService;
import ru.skillbox.deliveryservice.service.RequestSendingService;


@Component
public class OrderInventedEventHandler implements EventHandler<OrderInventedEvent, OrderDeliveredEvent> {

    private final DeliveryService deliveryService;
    private final RequestSendingService requestSendingService;
    private static final Logger logger = LoggerFactory.getLogger(OrderInventedEventHandler.class);

    @Autowired
    public OrderInventedEventHandler(DeliveryService deliveryService, RequestSendingService requestSendingService) {
        this.deliveryService = deliveryService;
        this.requestSendingService = requestSendingService;
    }

    @Transactional
    public OrderDeliveredEvent handleEvent(OrderInventedEvent event) {
        logger.info("Event handling: " + event);
        try {
            Thread.sleep(3000);
            if (event.getStatus().equals(OrderStatus.INVENTED)) {
                OrderDeliveredEvent deliveredEvent = OrderDeliveredEvent.builder()
                        .userId(event.getUserId())
                        .orderId(event.getOrderId())
                        .authHeaderValue(event.getAuthHeaderValue())
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
            } else {
                throw new NothingToDoException("Nothing to do!");
            }
        } catch (InterruptedException | NothingToDoException e) {
            throw new RuntimeException(e);
        }
    }
}
