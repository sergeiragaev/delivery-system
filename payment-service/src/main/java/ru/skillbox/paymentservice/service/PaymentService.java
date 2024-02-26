package ru.skillbox.paymentservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.paymentservice.domain.dto.SumDto;
import ru.skillbox.paymentservice.domain.model.UserBalance;
import ru.skillbox.paymentservice.repository.UserBalanceRepository;

import java.util.Optional;

@Slf4j
@Service
public class PaymentService {

    private final UserBalanceRepository userBalanceRepository;

    @Autowired
    public PaymentService(UserBalanceRepository userBalanceRepository) {
        this.userBalanceRepository = userBalanceRepository;
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
}
