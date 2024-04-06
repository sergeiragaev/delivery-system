package ru.skillbox.orderservice.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skillbox.orderservice.domain.OrderStatusHistory;
import ru.skillbox.orderservice.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByDescription(String desc);

    @Query("select h from OrderStatusHistory h where h.order.id = :id")
    List<OrderStatusHistory> findOrderStatusHistoryById(long id);
    @EntityGraph(attributePaths = "orderStatusHistory")
    List<Order> findAll();
}
