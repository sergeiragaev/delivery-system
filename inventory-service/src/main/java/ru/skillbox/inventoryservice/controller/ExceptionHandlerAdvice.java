package ru.skillbox.inventoryservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skillbox.inventoryservice.domain.dto.ErrorDto;
import ru.skillbox.inventoryservice.exception.InventoryNotFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler({InventoryNotFoundException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorDto> exceptionHandler(Exception ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setErrorMessage(ex.getMessage());
        errorDto.setTimestamp(LocalDateTime.now());

        return ResponseEntity.badRequest().body(errorDto);
    }
}
