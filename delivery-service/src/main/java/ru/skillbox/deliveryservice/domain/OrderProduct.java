package ru.skillbox.deliveryservice.domain;

import lombok.Data;

@Data
public class OrderProduct {
    private long productId;
    private int count;
}
