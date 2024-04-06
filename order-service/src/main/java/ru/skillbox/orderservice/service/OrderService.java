package ru.skillbox.orderservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.function.Tuple2;
import ru.skillbox.orderservice.exception.OrderNotFoundException;
import ru.skillbox.orderservice.domain.*;
import ru.skillbox.orderservice.domain.dto.OrderDto;
import ru.skillbox.orderservice.domain.dto.StatusDto;
import ru.skillbox.orderservice.domain.enums.OrderStatus;
import ru.skillbox.orderservice.processor.OrderProcessor;
import ru.skillbox.orderservice.repository.OrderRepository;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProcessor orderProcessor;
    private final Scheduler jdbcScheduler;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderProcessor orderProcessor, Scheduler jdbcScheduler) {
        this.orderRepository = orderRepository;
        this.orderProcessor = orderProcessor;
        this.jdbcScheduler = jdbcScheduler;
    }

    @Transactional
    public void updateOrderStatus(Long id, StatusDto statusDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        if (order.getStatus() == statusDto.getStatus()) {
            log.info("Request with same status {} for order {} from service {}", statusDto.getStatus(), id, statusDto.getServiceName());
            return;
        }
        order.setStatus(statusDto.getStatus());
        order.addStatusHistory(statusDto.getStatus(), statusDto.getServiceName(), statusDto.getComment());
        orderRepository.save(order);
    }

    @Transactional
    public Mono<Order> addOrder(OrderDto orderDto, Long userId) {
        Order newOrder = new Order(
                orderDto.getDepartureAddress(),
                orderDto.getDestinationAddress(),
                orderDto.getDescription(),
                orderDto.getCost(),
                OrderStatus.REGISTERED
        );
        newOrder.addProducts(orderDto.getProducts());
        Order order = orderRepository.save(newOrder);
        order.addStatusHistory(order.getStatus(), ServiceName.ORDER_SERVICE, "Order created");
        log.info("Order created: " + order);
        orderProcessor.process(order, userId);
        return Mono.just(order);
    }

    public Flux<Order> getAll() {
        return Flux.defer(
                        () -> Flux.fromIterable(orderRepository.findAll()))
                .subscribeOn(jdbcScheduler);
    }

    public Flux<List<Order>> reactiveGetAll() {
        Flux<Long> interval = Flux.interval(Duration.ofMillis(5000));
        Flux<List<Order>> orderFlux = Flux.fromStream(
                Stream.generate(orderRepository::findAll));
        return Flux.zip(interval, orderFlux)
                .map(Tuple2::getT2);
    }

}
