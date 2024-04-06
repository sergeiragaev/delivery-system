package ru.skillbox.orderservice.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDto {

    private String errorMessage;
    private LocalDateTime timestamp;
}
