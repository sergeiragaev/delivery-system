package ru.skillbox.orderservice.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.skillbox.orderservice.domain.event.TransactionEvent;
import ru.skillbox.orderservice.domain.model.Order;
import ru.skillbox.orderservice.repository.OrderRepository;

import static ru.skillbox.orderservice.domain.enums.OrderStatus.COMPLETED;
import static ru.skillbox.orderservice.domain.enums.OrderStatus.FAILED;
import static ru.skillbox.orderservice.domain.enums.TransactionStatus.SUCCESSFUL;


@Component
public class TransactionEventConsumer implements EventConsumer<TransactionEvent> {

    private final OrderRepository orderRepository;
    private final Scheduler jdbcScheduler;

    @Autowired
    public TransactionEventConsumer(
            OrderRepository orderRepository,
            Scheduler jdbcScheduler) {
        this.orderRepository = orderRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    public void consumeEvent(TransactionEvent event) {
        Mono.fromRunnable(
                () -> orderRepository.findById(event.getOrderId())
                        .ifPresent(order -> {
                            setStatus(event, order);
                            orderRepository.save(order);
                        }))
                .subscribeOn(jdbcScheduler)
                .subscribe();
    }

    private void setStatus(TransactionEvent transactionEvent, Order order) {
        order.setStatus(SUCCESSFUL.equals(transactionEvent.getStatus())
                ? COMPLETED
                : FAILED);
    }

}
