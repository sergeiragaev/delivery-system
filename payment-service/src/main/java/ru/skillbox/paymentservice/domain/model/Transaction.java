package ru.skillbox.paymentservice.domain.model;

import jakarta.persistence.*;
import lombok.*;
import ru.skillbox.paymentservice.domain.enums.PaymentStatus;


@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;
    private long orderId;
    private long cost;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
