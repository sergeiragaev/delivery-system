package ru.skillbox.paymentservice.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.skillbox.paymentservice.domain.event.OrderInventEvent;
import ru.skillbox.paymentservice.domain.model.Transaction;
import ru.skillbox.paymentservice.domain.model.UserBalance;
import ru.skillbox.paymentservice.repository.TransactionRepository;
import ru.skillbox.paymentservice.repository.UserBalanceRepository;

import java.text.MessageFormat;

import static ru.skillbox.paymentservice.domain.enums.OrderStatus.INVENTMENT_FAILED;
import static ru.skillbox.paymentservice.domain.enums.OrderStatus.PAID;


@Component
public class InventoryEventConsumer implements EventConsumer<OrderInventEvent> {

    private final TransactionRepository transactionRepository;
    private final UserBalanceRepository userBalanceRepository;
    private final Scheduler jdbcScheduler;
    private static final Logger logger = LoggerFactory.getLogger(InventoryEventConsumer.class);

    @Autowired
    public InventoryEventConsumer(
            TransactionRepository transactionRepository, UserBalanceRepository userBalanceRepository, Scheduler jdbcScheduler) {
        this.transactionRepository = transactionRepository;
        this.userBalanceRepository = userBalanceRepository;
        this.jdbcScheduler = jdbcScheduler;
    }
    private void returnPayment(Transaction transaction, long userId) {
        userBalanceRepository
                .findByUserId(userId)
                .ifPresent(userBalance -> returnToUserBalance(userBalance, transaction.getCost()));
                }

    private void returnToUserBalance(UserBalance userBalance, long cost) {
        userBalance.setBalance(userBalance.getBalance() + cost);
        userBalanceRepository.save(userBalance);
    }

    @Override
    public void consumeEvent(OrderInventEvent event) {
        logger.info(MessageFormat.format("Event consume: {0}", event));
        if (INVENTMENT_FAILED.equals(event.getStatus())) {
            Mono.fromRunnable(
                            () -> transactionRepository.findByOrderIdAndStatus(event.getOrderId(), PAID)
                                    .ifPresent(transaction ->
                                            returnPayment(transaction, event.getUserId())
                                    ))
                    .subscribeOn(jdbcScheduler)
                    .subscribe();
        }
    }
}
