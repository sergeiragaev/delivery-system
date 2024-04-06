package ru.skillbox.paymentservice.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.skillbox.paymentservice.domain.event.TransactionEvent;
import ru.skillbox.paymentservice.domain.model.Payments;
import ru.skillbox.paymentservice.repository.PaymentRepository;
import ru.skillbox.paymentservice.repository.UserBalanceRepository;

import static ru.skillbox.paymentservice.domain.enums.TransactionStatus.UNSUCCESSFUL;


@Component
public class TransactionEventConsumer implements EventConsumer<TransactionEvent> {

    private final PaymentRepository transactionRepository;
    private final UserBalanceRepository userBalanceRepository;
    private final Scheduler jdbcScheduler;

    @Autowired
    public TransactionEventConsumer(
            PaymentRepository transactionRepository, UserBalanceRepository userBalanceRepository, Scheduler jdbcScheduler) {
        this.transactionRepository = transactionRepository;
        this.userBalanceRepository = userBalanceRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    public void consumeEvent(TransactionEvent event) {
        if (UNSUCCESSFUL.equals(event.getStatus()))
            Mono.fromRunnable(
                    () -> transactionRepository.findByOrderId(event.getOrderId())
                            .ifPresent(transaction -> {
                                transactionRepository.delete(transaction);
                                resetPayment(transaction, event.getUserId());
                            }))
                                    .subscribeOn(jdbcScheduler)
                                    .subscribe();
    }

    private void resetPayment(Payments transaction, long userId) {
        userBalanceRepository
                .findByUserId(userId)
                .ifPresent(userBalance -> {
                            userBalance.setBalance(userBalance.getBalance() + transaction.getCost());
                            userBalanceRepository.save(userBalance);
                        }
                );
    }
}
