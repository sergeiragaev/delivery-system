package ru.skillbox.paymentservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.paymentservice.domain.enums.OrderStatus;
import ru.skillbox.paymentservice.domain.ServiceName;

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
