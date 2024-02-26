package ru.skillbox.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.skillbox.orderservice.domain.dto.OrderDto;
import ru.skillbox.orderservice.domain.dto.StatusDto;
import ru.skillbox.orderservice.exception.OrderNotFoundException;
import ru.skillbox.orderservice.repository.OrderRepository;
import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.service.OrderService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderRepository orderRepository;

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }
    @Operation(summary = "List all orders in delivery system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public Flux<Order> getAllOrders() {
        return orderService.getAll();
    }

    @Operation(summary = "Get an order in system by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{orderId}")
    public Order listOrder(@PathVariable @Parameter(description = "Id of order") Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }


    @Operation(summary = "Update order status", security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable @Parameter(description = "Id of order") long orderId,
                                               @RequestBody StatusDto statusDto) {
        try {
            orderService.updateOrderStatus(orderId, statusDto);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Can't change status for order with id {}", orderId, ex);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }
    @Operation(summary = "Add order and start delivery process for it", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public Mono<Order> addOrder(@RequestBody OrderDto input, HttpServletRequest request) {
        return orderService.addOrder(input, Long.valueOf(request.getHeader("id")));
    }

    @Operation(summary = "Stream orders in delivery system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<Order>> getAllOrdersStream() {
        return orderService.reactiveGetAll();
    }

}
