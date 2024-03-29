package ru.skillbox.orderservice.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.orderservice.domain.model.OrderStatusHistory;
import ru.skillbox.orderservice.domain.model.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByDescription(String desc);

    @Query("select h from OrderStatusHistory h where h.order.id = :id order by h.id")
    List<OrderStatusHistory> findOrderStatusHistoryById(long id);
    @EntityGraph(attributePaths = "orderStatusHistory")
    List<Order> findAll();
}
