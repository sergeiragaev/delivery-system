package ru.skillbox.inventoryservice.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.skillbox.inventoryservice.domain.dto.ProductDto;
import ru.skillbox.inventoryservice.domain.event.TransactionEvent;
import ru.skillbox.inventoryservice.domain.model.Invents;
import ru.skillbox.inventoryservice.domain.model.InventsProduct;
import ru.skillbox.inventoryservice.repository.InventsRepository;
import ru.skillbox.inventoryservice.service.InventoryService;

import java.util.ArrayList;
import java.util.List;

import static ru.skillbox.inventoryservice.domain.enums.TransactionStatus.UNSUCCESSFUL;


@Component
public class TransactionEventConsumer implements EventConsumer<TransactionEvent> {

    private final InventsRepository transactionRepository;
    private final InventoryService inventoryService;
    private final Scheduler jdbcScheduler;

    @Autowired
    public TransactionEventConsumer(
            InventsRepository transactionRepository, InventoryService inventoryService, Scheduler jdbcScheduler) {
        this.transactionRepository = transactionRepository;
        this.inventoryService = inventoryService;
        this.jdbcScheduler = jdbcScheduler;
    }

    public void consumeEvent(TransactionEvent event) {
        if (UNSUCCESSFUL.equals(event.getStatus())) {
            Mono.fromRunnable(
                            () -> transactionRepository.findByOrderId(event.getOrderId())
                                    .ifPresent(invents -> {
                                        transactionRepository.delete(invents);
                                        returnOrderToInventory(invents);
                                    }))
                    .subscribeOn(jdbcScheduler)
                    .subscribe();
        }
    }

    private void returnOrderToInventory(Invents invents) {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (InventsProduct product : invents.getProducts()) {
            ProductDto productDto = new ProductDto();
            productDto.setProductId(product.getProductId());
            productDto.setCount(product.getCount());
            productDtoList.add(productDto);
        }
        inventoryService.addProducts(productDtoList);
    }
}
