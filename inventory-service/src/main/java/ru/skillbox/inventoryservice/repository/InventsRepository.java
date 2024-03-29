package ru.skillbox.inventoryservice.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.inventoryservice.domain.model.Invents;

import java.util.Optional;

@Repository
public interface InventsRepository extends JpaRepository<Invents, Long> {
    @EntityGraph(attributePaths = "products")
    Optional<Invents> findByOrderId(Long orderId);
}
