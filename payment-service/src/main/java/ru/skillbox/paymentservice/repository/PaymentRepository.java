package ru.skillbox.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.paymentservice.domain.model.Payments;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payments, Long> {
    Optional<Payments> findByOrderId(Long orderId);
}
