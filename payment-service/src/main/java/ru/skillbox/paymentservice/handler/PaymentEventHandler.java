package ru.skillbox.paymentservice.handler;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.skillbox.paymentservice.domain.PaymentEvent;
import ru.skillbox.paymentservice.domain.model.Transaction;
import ru.skillbox.paymentservice.domain.TransactionEvent;
import ru.skillbox.paymentservice.repository.TransactionRepository;

import static ru.skillbox.paymentservice.domain.enums.PaymentStatus.APPROVED;
import static ru.skillbox.paymentservice.domain.enums.TransactionStatus.SUCCESSFUL;
import static ru.skillbox.paymentservice.domain.enums.TransactionStatus.UNSUCCESSFUL;


@Component
public class PaymentEventHandler implements EventHandler<PaymentEvent, TransactionEvent> {

    private final TransactionRepository transactionRepository;
    private final Scheduler jdbcScheduler;

    @Autowired
    public PaymentEventHandler(
            TransactionRepository transactionRepository,
            Scheduler jdbcScheduler) {
        this.transactionRepository = transactionRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    @Transactional
    public TransactionEvent handleEvent(PaymentEvent event) {
        Mono.fromRunnable(() -> transactionRepository.save(
                Transaction.builder()
                        .orderId(event.getOrderId())
                        .cost(event.getCost())
                        .status(event.getStatus())
                        .build()))
                .subscribeOn(jdbcScheduler)
                .subscribe();

        return new TransactionEvent()
                .orderId(event.getOrderId())
                .status(APPROVED.equals(event.getStatus())
                        ? SUCCESSFUL
                        : UNSUCCESSFUL);

    }
}
