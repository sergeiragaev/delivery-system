package ru.skillbox.paymentservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.paymentservice.domain.dto.SumDto;
import ru.skillbox.paymentservice.domain.model.UserBalance;
import ru.skillbox.paymentservice.repository.UserBalanceRepository;
import ru.skillbox.paymentservice.service.PaymentService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserBalanceRepository userBalanceRepository;

    @MockBean
    private PaymentService paymentService;


    @Configuration
    @ComponentScan(basePackageClasses = {PaymentController.class})
    public static class TestConf {
    }

    private UserBalance userBalance;

    private UserBalance newUserBalance;

    private List<UserBalance> balances;

    @BeforeEach
    public void setUp() {
        userBalance = new UserBalance();
        userBalance.setUserId(1L);
        userBalance.setBalance(1500L);

        newUserBalance = new UserBalance();
        newUserBalance.setUserId(2L);
        newUserBalance.setBalance(3000L);

        balances = Collections.singletonList(newUserBalance);

    }

    @Test
    void addSumToUserBalance() throws Exception {
        SumDto sumDto = new SumDto();
        sumDto.setSum(1500);

        Mockito.when(paymentService.fillBalance(1L, sumDto)).thenReturn(userBalance);
        mvc.perform(
                        post("/balance")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"sum\":1500}"
                                )
                                .header("id", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(userBalance.getBalance().toString())));
    }

    @Test
    void listBalances() throws Exception {
        Mockito.when(userBalanceRepository.findAll()).thenReturn(balances);
        mvc.perform(get("/balance"))
                .andExpect(status().isOk())
                .andExpect(
                        content().string(containsString(newUserBalance.getBalance().toString())));
    }

    @Test
    void getBalanceByUserId() throws Exception {
        Mockito.when(userBalanceRepository.findByUserId(1L)).thenReturn(Optional.of(userBalance));
        mvc.perform(get("/balance/1"))
                .andExpect(status().isOk())
                .andExpect(
                        content().string(containsString(userBalance.getBalance().toString())));

        mvc.perform(get("/balance/2"))
                .andExpect(status().isBadRequest())
                .andExpect(
                        content().string(containsString(userBalance.getUserId().toString())));

    }

}