package ru.skillbox.orderservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.orderservice.domain.ServiceName;
import ru.skillbox.orderservice.domain.enums.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDto {

    private OrderStatus status;

    private ServiceName serviceName;

    private String comment;

    public StatusDto(OrderStatus status) {
        this.status = status;
    }
}
