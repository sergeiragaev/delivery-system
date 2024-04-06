package ru.skillbox.orderservice.domain.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import ru.skillbox.orderservice.domain.model.OrderProduct;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderDto {

    private String description;

    private String departureAddress;

    private String destinationAddress;

    private Long cost;

    private List<OrderProduct> products;
}
