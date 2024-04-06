package ru.skillbox.paymentservice.handler;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skillbox.paymentservice.domain.OrderCreatedEvent;
import ru.skillbox.paymentservice.domain.PaymentEvent;
import ru.skillbox.paymentservice.domain.model.UserBalance;
import ru.skillbox.paymentservice.repository.UserBalanceRepository;

import static ru.skillbox.paymentservice.domain.enums.PaymentStatus.APPROVED;
import static ru.skillbox.paymentservice.domain.enums.PaymentStatus.DECLINED;

@Component
public class OrderCreatedEventHandler implements EventHandler<OrderCreatedEvent, PaymentEvent> {

    private final UserBalanceRepository userBalanceRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderCreatedEventHandler.class);

    @Autowired
    public OrderCreatedEventHandler(UserBalanceRepository userBalanceRepository) {
        this.userBalanceRepository = userBalanceRepository;
    }

    @Transactional
    public PaymentEvent handleEvent(OrderCreatedEvent event) {
        logger.info("Event handling: " + event.getEvent());
        Long orderCost = event.getCost();
        Long userId = event.getUserId();
        PaymentEvent paymentEvent = PaymentEvent.builder()
                .orderId(event.getOrderId())
                .cost(event.getCost())
                .status(DECLINED)
                .build();
        userBalanceRepository
                .findByUserId(userId)
                .ifPresent(user -> deductUserBalance(orderCost, paymentEvent, user));
        return paymentEvent;
    }

    private void deductUserBalance(Long orderPrice, PaymentEvent paymentEvent, UserBalance user) {
        Long userBalance = user.getBalance();
        if (userBalance >= orderPrice) {
            user.setBalance(userBalance - orderPrice);
            userBalanceRepository.save(user);
            paymentEvent.setStatus(APPROVED);
        }
    }

}
