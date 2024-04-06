package ru.skillbox.paymentservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.skillbox.paymentservice.domain.model.UserBalance;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class UserBalanceRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @Test
    void whenGetByUserId_thenReturnBalance() {
        UserBalance balance = new UserBalance();
        balance.setUserId(1L);
        balance.setBalance(1500L);

        userBalanceRepository.save(balance);
        entityManager.persist(balance);
        entityManager.flush();

        long userId = 1L;
        UserBalance gotBalance = userBalanceRepository.findByUserId(userId).get();

        assertThat(gotBalance.getBalance())
                .isEqualTo(balance.getBalance());
        assertThat(userBalanceRepository.count())
                .isEqualTo(1);
        userBalanceRepository.delete(gotBalance);
        assertThat(userBalanceRepository.count())
                .isZero();
    }

}