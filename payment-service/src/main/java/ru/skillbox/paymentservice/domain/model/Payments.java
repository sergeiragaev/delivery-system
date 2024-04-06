package ru.skillbox.paymentservice.domain.model;

import jakarta.persistence.*;
import lombok.*;


@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long orderId;
    private long cost;

}
