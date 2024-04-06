package ru.skillbox.inventoryservice.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.skillbox.inventoryservice.domain.enums.TransactionStatus;
import ru.skillbox.inventoryservice.domain.event.InventoryEvent;
import ru.skillbox.inventoryservice.domain.event.TransactionEvent;
import ru.skillbox.inventoryservice.domain.model.Invents;
import ru.skillbox.inventoryservice.repository.InventsRepository;

import static ru.skillbox.inventoryservice.domain.enums.OrderStatus.INVENTED;


@Component
public class InventoryEventHandler implements EventHandler<InventoryEvent, TransactionEvent> {

    private final InventsRepository inventsRepository;
    private final Scheduler jdbcScheduler;

    @Autowired
    public InventoryEventHandler(
            InventsRepository transactionRepository,
            Scheduler jdbcScheduler) {
        this.inventsRepository = transactionRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    @Transactional
    public TransactionEvent handleEvent(InventoryEvent event) {

        if (INVENTED.equals(event.getStatus())) {

            Invents newInvents = new Invents(
                    event.getOrderId());
            newInvents.addProducts(event.getProducts());


            Mono.fromRunnable(() -> inventsRepository.save(
                            newInvents))
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
