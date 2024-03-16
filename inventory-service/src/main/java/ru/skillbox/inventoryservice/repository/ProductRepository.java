package ru.skillbox.inventoryservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.inventoryservice.domain.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
