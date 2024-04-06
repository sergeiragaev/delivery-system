package ru.skillbox.inventoryservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import ru.skillbox.inventoryservice.domain.dto.ProductDto;
import ru.skillbox.inventoryservice.domain.enums.OrderStatus;
import ru.skillbox.inventoryservice.domain.event.InventoryEvent;
import ru.skillbox.inventoryservice.domain.model.Product;
import ru.skillbox.inventoryservice.domain.model.InventsProduct;
import ru.skillbox.inventoryservice.exception.InventoryNotFoundException;
import ru.skillbox.inventoryservice.exception.NotEnoughInventoryException;
import ru.skillbox.inventoryservice.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class InventoryService {

    private final ProductRepository productRepository;

    @Autowired
    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> addProducts(List<ProductDto> productDtoList) {
        List<Product> products = new ArrayList<>();
        for (ProductDto productDto : productDtoList) {
            long productId = productDto.getProductId();
            Optional<Product> optionalProduct = productRepository.findById(productId);
            Product product;
            if (optionalProduct.isEmpty()) {
                product = new Product();
                product.setProductId(productId);
                product.setCount(productDto.getCount());
            } else {
                product = optionalProduct.get();
                product.setCount(product.getCount() + productDto.getCount());
            }
            products.add(productRepository.save(product));
        }
        return products;
    }

    @Transactional
    public String inventOrder(InventoryEvent event) {
        try {
            for (InventsProduct orderProduct : event.getProducts()) {
                long productId = orderProduct.getProductId();
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new InventoryNotFoundException("Product with ID=" + productId + " has not found!"));
                deductCount(product, orderProduct.getCount());
            }

            return "Order invented successfully";
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            event.setStatus(OrderStatus.INVENTMENT_FAILED);
            return e.getMessage();
        }
    }

    private void deductCount(Product product, int count) throws NotEnoughInventoryException {
        int productCount = product.getCount();
        if (productCount >= count) {
            product.setCount(productCount - count);
            productRepository.save(product);
        } else {
            throw new NotEnoughInventoryException("Count of product with ID=" + product.getProductId() + " is not enough!");
        }
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
