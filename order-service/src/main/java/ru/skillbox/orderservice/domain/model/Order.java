package ru.skillbox.orderservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import ru.skillbox.orderservice.domain.ServiceName;
import ru.skillbox.orderservice.domain.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "departure_address")
    private String departureAddress;

    @Column(name = "destination_address")
    private String destinationAddress;

    @Column(name = "cost")
    private Long cost;

    @CreationTimestamp
    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @UpdateTimestamp
    @Column(name = "modified_time")
    private LocalDateTime modifiedTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderStatusHistory> orderStatusHistory = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderProduct> products = new ArrayList<>();

    public Order(
            String departureAddress,
            String destinationAddress,
            String description,
            Long cost,
            OrderStatus status
    ) {
        this.departureAddress = departureAddress;
        this.destinationAddress = destinationAddress;
        this.description = description;
        this.cost = cost;
        this.status = status;
    }

    public void addStatusHistory(OrderStatus status, ServiceName serviceName, String comment) {
        getOrderStatusHistory().add(new OrderStatusHistory(null, status, serviceName, comment, this));
    }

    public void addProducts(List<OrderProduct> productsFromInput) {
        List<OrderProduct> productDetailList = new ArrayList<>();
        productsFromInput.forEach(product ->
                productDetailList.add(new OrderProduct(null, product.getProductId(), product.getCount(), this)));

        getProducts().addAll(productDetailList);
    }

}
