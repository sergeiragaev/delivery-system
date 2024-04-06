package ru.skillbox.paymentservice.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.Min;

@Data
public class SumDto {

    @Min(1)
    private Integer sum;
}
