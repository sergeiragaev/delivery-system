package ru.skillbox.paymentservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.paymentservice.domain.dto.SumDto;
import ru.skillbox.paymentservice.domain.event.OrderPaidEvent;
import ru.skillbox.paymentservice.domain.model.Transaction;
import ru.skillbox.paymentservice.domain.model.UserBalance;
import ru.skillbox.paymentservice.exception.BalanceNotFoundException;
import ru.skillbox.paymentservice.exception.InsufficientFundsException;
import ru.skillbox.paymentservice.repository.TransactionRepository;
import ru.skillbox.paymentservice.repository.UserBalanceRepository;

import java.util.Optional;

import static ru.skillbox.paymentservice.domain.enums.OrderStatus.PAID;

@Slf4j
@Service
public class PaymentService {

    private final UserBalanceRepository userBalanceRepository;
    private final TransactionRepository transactionRepository;


    @Autowired
    public PaymentService(UserBalanceRepository userBalanceRepository, TransactionRepository transactionRepository) {
        this.userBalanceRepository = userBalanceRepository;
        this.transactionRepository = transactionRepository;
    }

    public UserBalance fillBalance(Long userId, SumDto sumDto) {
        Optional<UserBalance> optionalBalance = userBalanceRepository.findByUserId(userId);
        UserBalance balance;
        if (optionalBalance.isEmpty()) {
            balance = new UserBalance();
            balance.setUserId(userId);
            balance.setBalance(sumDto.getSum().longValue());
        } else {
            balance = optionalBalance.get();
            balance.setBalance(balance.getBalance() + sumDto.getSum());
        }
        return userBalanceRepository.save(balance);
    }

    private void deductUserBalance(Long orderPrice, OrderPaidEvent orderPaidEvent, UserBalance user) {
        Long userBalance = user.getBalance();
        if (userBalance >= orderPrice) {
            user.setBalance(userBalance - orderPrice);
            userBalanceRepository.save(user);
            orderPaidEvent.setStatus(PAID);
        } else {
            throw new InsufficientFundsException("User with ID: " + user.getUserId() + " has not enough funds!");
        }
    }

    public String payOrder(OrderPaidEvent orderPaidEvent) {
        try {
            Long orderCost = orderPaidEvent.getCost();
            Long userId = orderPaidEvent.getUserId();
            UserBalance userBalance = userBalanceRepository
                    .findByUserId(userId)
                    .orElseThrow(() -> new BalanceNotFoundException("No balance record for user with ID: " + userId));

            deductUserBalance(orderCost, orderPaidEvent, userBalance);
            transactionRepository.save(
                    Transaction.builder()
                            .orderId(orderPaidEvent.getOrderId())
                            .cost(orderPaidEvent.getCost())
                            .status(orderPaidEvent.getStatus())
                            .build());
            return "Payment successful";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
