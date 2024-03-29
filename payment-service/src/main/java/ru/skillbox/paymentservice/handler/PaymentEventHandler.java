package ru.skillbox.paymentservice.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.skillbox.paymentservice.domain.enums.TransactionStatus;
import ru.skillbox.paymentservice.domain.event.PaymentEvent;
import ru.skillbox.paymentservice.domain.event.TransactionEvent;
import ru.skillbox.paymentservice.domain.model.Payments;
import ru.skillbox.paymentservice.repository.PaymentRepository;

import static ru.skillbox.paymentservice.domain.enums.OrderStatus.PAID;


@Component
public class PaymentEventHandler implements EventHandler<PaymentEvent, TransactionEvent> {

    private final PaymentRepository paymentRepository;
    private final Scheduler jdbcScheduler;

    @Autowired
    public PaymentEventHandler(
            PaymentRepository transactionRepository,
            Scheduler jdbcScheduler) {
        this.paymentRepository = transactionRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    @Transactional
    public TransactionEvent handleEvent(PaymentEvent event) {

        if (PAID.equals(event.getStatus())) {

            Mono.fromRunnable(() -> paymentRepository.save(
                            Payments.builder()
                                    .orderId(event.getOrderId())
                                    .cost(event.getCost())
                                    .build()))
                    .subscribeOn(jdbcScheduler)
                    .subscribe();

            return null;
        }

        return TransactionEvent.builder()
                .orderId(event.getOrderId())
                .userId(event.getUserId())
                .status(TransactionStatus.UNSUCCESSFUL)
                .build();

    }
}
