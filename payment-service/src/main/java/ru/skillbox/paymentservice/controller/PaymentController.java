package ru.skillbox.paymentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.paymentservice.domain.dto.SumDto;
import ru.skillbox.paymentservice.domain.model.UserBalance;
import ru.skillbox.paymentservice.exception.BalanceNotFoundException;
import ru.skillbox.paymentservice.repository.UserBalanceRepository;
import ru.skillbox.paymentservice.service.PaymentService;

import java.util.List;

@Slf4j
@RestController
public class PaymentController {

    private final PaymentService paymentService;
    private final UserBalanceRepository userBalanceRepository;

    @Autowired
    public PaymentController(PaymentService paymentService, UserBalanceRepository userBalanceRepository) {
        this.paymentService = paymentService;
        this.userBalanceRepository = userBalanceRepository;
    }

    @Operation(summary = "Add money to user balance", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/balance")
    public ResponseEntity<UserBalance> fillingBalance(@RequestBody SumDto input, HttpServletRequest request) {
        return ResponseEntity.ok(paymentService.fillBalance(Long.valueOf(request.getHeader("id")), input));
    }
    @GetMapping("/balance")
    public ResponseEntity<List<UserBalance>> getBalances() {
        return ResponseEntity.ok(userBalanceRepository.findAll());
    }
    @GetMapping("/balance/{userId}")
    public ResponseEntity<UserBalance> getBalanceByUserId(@PathVariable @Parameter(description = "Id of user") long userId) throws BalanceNotFoundException {
            return ResponseEntity.ok(userBalanceRepository.findByUserId(userId)
                    .orElseThrow(() -> new BalanceNotFoundException("No balance record for user with ID=" + userId)));
    }

}
