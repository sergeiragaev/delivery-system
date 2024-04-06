package ru.skillbox.paymentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.paymentservice.domain.dto.SumDto;
import ru.skillbox.paymentservice.domain.model.UserBalance;
import ru.skillbox.paymentservice.service.PaymentService;

@Slf4j
@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Add money to user balance", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/balance")
    public ResponseEntity<UserBalance> fillingBalance(@RequestBody SumDto input, HttpServletRequest request) {
        return ResponseEntity.ok(paymentService.fillBalance(Long.valueOf(request.getHeader("id")), input));
    }
}
