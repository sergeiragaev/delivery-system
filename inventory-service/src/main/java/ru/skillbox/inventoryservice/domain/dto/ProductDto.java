package ru.skillbox.inventoryservice.domain.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ProductDto {

    private Long productId;
    @Min(1)
    private Integer count;
}
