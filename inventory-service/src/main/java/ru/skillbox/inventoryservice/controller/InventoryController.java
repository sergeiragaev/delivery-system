package ru.skillbox.inventoryservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.inventoryservice.domain.dto.ProductDto;
import ru.skillbox.inventoryservice.domain.model.Product;
import ru.skillbox.inventoryservice.exception.InventoryNotFoundException;
import ru.skillbox.inventoryservice.repository.ProductRepository;
import ru.skillbox.inventoryservice.service.InventoryService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final ProductRepository productRepository;

    @Autowired
    public InventoryController(InventoryService inventoryService, ProductRepository productRepository) {
        this.inventoryService = inventoryService;
        this.productRepository = productRepository;
    }

    @Operation(summary = "Add products to inventory")
    @PostMapping("/products")
    public ResponseEntity<List<Product>> addProducts(@RequestBody @Valid List<ProductDto> input) {
        return ResponseEntity.ok(inventoryService.addProducts(input));
    }
    @Operation(summary = "List all products from inventory")
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(inventoryService.getAllProducts());
    }

    @Operation(summary = "Get product's information by it's id")
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable long id) throws InventoryNotFoundException {
        return ResponseEntity.ok(productRepository.findProductByProductId(id)
                .orElseThrow(() ->
                        new InventoryNotFoundException("No information about product with ID=" + id)));
    }

}
