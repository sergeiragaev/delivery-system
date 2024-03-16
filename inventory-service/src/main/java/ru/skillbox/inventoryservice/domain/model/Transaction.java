package ru.skillbox.inventoryservice.domain.model;

import jakarta.persistence.*;
import lombok.*;
import ru.skillbox.inventoryservice.domain.enums.OrderStatus;


@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long orderId;
    private long cost;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
