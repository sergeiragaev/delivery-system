package ru.skillbox.paymentservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.skillbox.paymentservice.domain.model.Payments;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class PaymentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PaymentRepository paymentRepositoryJpa;

    @Test
    void whenGetByOrderId_thenReturnPayment() {
        Payments payment = Payments.builder()
                .orderId(1L).cost(1500L)
                .build();
        paymentRepositoryJpa.save(payment);
        entityManager.persist(payment);
        entityManager.flush();

        long orderId = 1L;
        Payments gotPayment = paymentRepositoryJpa.findByOrderId(orderId).get();

        assertThat(gotPayment.getCost())
                .isEqualTo(payment.getCost());
        assertThat(paymentRepositoryJpa.count())
                .isEqualTo(1);
        paymentRepositoryJpa.delete(gotPayment);
        assertThat(paymentRepositoryJpa.count())
                .isZero();
    }

}
