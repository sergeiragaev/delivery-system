package ru.skillbox.paymentservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.paymentservice.domain.event.OrderCreatedEvent;
import ru.skillbox.paymentservice.domain.event.OrderPaidEvent;
import ru.skillbox.paymentservice.domain.model.Transaction;
import ru.skillbox.paymentservice.domain.model.UserBalance;
import ru.skillbox.paymentservice.exception.BalanceNotFoundException;
import ru.skillbox.paymentservice.exception.InsufficientFundsException;
import ru.skillbox.paymentservice.repository.TransactionRepository;
import ru.skillbox.paymentservice.repository.UserBalanceRepository;

import static ru.skillbox.paymentservice.domain.enums.OrderStatus.PAID;
import static ru.skillbox.paymentservice.domain.enums.OrderStatus.PAYMENT_FAILED;

@Component
public class OrderCreatedEventHandler implements EventHandler<OrderCreatedEvent, OrderPaidEvent> {

    private final UserBalanceRepository userBalanceRepository;
    private final TransactionRepository transactionRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrderCreatedEventHandler.class);

    @Autowired
    public OrderCreatedEventHandler(UserBalanceRepository userBalanceRepository, TransactionRepository transactionRepository) {
        this.userBalanceRepository = userBalanceRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public OrderPaidEvent handleEvent(OrderCreatedEvent event) {
        logger.info("Event handling: " + event);
        try {
            Thread.sleep(3000);
            Long orderCost = event.getCost();
            Long userId = event.getUserId();
            OrderPaidEvent orderPaidEvent = OrderPaidEvent.builder()
                    .orderId(event.getOrderId())
                    .userId(userId)
                    .products(event.getProducts())
                    .status(PAYMENT_FAILED)
                    .destinationAddress(event.getDestinationAddress())
                    .authHeaderValue(event.getAuthHeaderValue())
                    .build();
            UserBalance userBalance = userBalanceRepository
                    .findByUserId(userId)
                    .orElseThrow(() -> new BalanceNotFoundException("No balance record for user with ID: " + userId));

            deductUserBalance(orderCost, orderPaidEvent, userBalance);
            transactionRepository.save(
                    Transaction.builder()
                            .orderId(event.getOrderId())
                            .cost(event.getCost())
                            .status(orderPaidEvent.getStatus())
                            .build());
            return orderPaidEvent;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void deductUserBalance(Long orderPrice, OrderPaidEvent orderPaidEvent, UserBalance user) {
        Long userBalance = user.getBalance();
        if (userBalance >= orderPrice) {
            user.setBalance(userBalance - orderPrice);
            userBalanceRepository.save(user);
            orderPaidEvent.setStatus(PAID);
        } else {
            throw new InsufficientFundsException("User with ID: " + user.getUserId() + " has not enough funds!");
        }
    }

}
