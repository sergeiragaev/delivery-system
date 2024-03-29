package ru.skillbox.paymentservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.paymentservice.domain.ServiceName;
import ru.skillbox.paymentservice.domain.dto.StatusDto;
import ru.skillbox.paymentservice.domain.event.OrderCreatedEvent;
import ru.skillbox.paymentservice.domain.event.PaymentEvent;
import ru.skillbox.paymentservice.service.PaymentService;
import ru.skillbox.paymentservice.service.RequestSendingService;

import static ru.skillbox.paymentservice.domain.enums.OrderStatus.PAYMENT_FAILED;

@Component
public class OrderCreatedEventHandler implements EventHandler<OrderCreatedEvent, PaymentEvent> {

    private final RequestSendingService requestSendingService;
    private final PaymentService paymentService;

    private static final Logger logger = LoggerFactory.getLogger(OrderCreatedEventHandler.class);

    @Autowired
    public OrderCreatedEventHandler(RequestSendingService requestSendingService, PaymentService paymentService) {
        this.requestSendingService = requestSendingService;
        this.paymentService = paymentService;
    }

    @Transactional
    public PaymentEvent handleEvent(OrderCreatedEvent event) {
        logger.info("Event handling: " + event);
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        PaymentEvent orderPaidEvent = PaymentEvent.builder()
                .orderId(event.getOrderId())
                .userId(event.getUserId())
                .cost(event.getCost())
                .products(event.getProducts())
                .status(PAYMENT_FAILED)
                .destinationAddress(event.getDestinationAddress())
                .authHeaderValue(event.getAuthHeaderValue())
                .build();

        String comment = paymentService.payOrder(orderPaidEvent);

        StatusDto statusDto = new StatusDto();
        statusDto.setStatus(orderPaidEvent.getStatus());
        statusDto.setServiceName(ServiceName.PAYMENT_SERVICE);
        statusDto.setComment(comment);
        requestSendingService.updateOrderStatusInOrderService(event.getOrderId(), statusDto, event.getAuthHeaderValue());

        return orderPaidEvent;
    }

}
