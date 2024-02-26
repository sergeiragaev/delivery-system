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
import ru.skillbox.paymentservice.domain.enums.OrderStatus;
import ru.skillbox.paymentservice.domain.dto.StatusDto;
import ru.skillbox.paymentservice.service.PaymentService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(PaymentController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PaymentRepository paymentRepository;

    @MockBean
    private PaymentService paymentService;

    @Configuration
    @ComponentScan(basePackageClasses = {PaymentController.class})
    public static class TestConf {
    }

    private Order order;

    private Order newOrder;

    private List<Order> orders;

    @BeforeEach
    public void setUp() {
        order = new Order(
                "Moscow, st.Taganskaya 150",
                "Moscow, st.Tulskaya 24",
                "Order #112",
                1500L,
                OrderStatus.REGISTERED
        );
        newOrder = new Order(
                "Moscow, st.Taganskaya 150",
                "Moscow, st.Dubininskaya 39",
                "Order #342",
                2450L,
                OrderStatus.REGISTERED
        );
        orders = Collections.singletonList(order);


    }

    @Test
    public void listOrders() throws Exception {
        Mockito.when(paymentRepository.findAll()).thenReturn(orders);
        mvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(order.getDescription())));
    }

    @Test
    public void listOrder() throws Exception {
        Mockito.when(paymentRepository.findById(1L)).thenReturn(Optional.of(order));
        mvc.perform(get("/order/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(order.getDescription())));
    }

    @Test
    public void addOrder() throws Exception {
        OrderDto orderDto = new OrderDto(
                "Order #342",
                "Moscow, st.Taganskaya 150",
                "Moscow, st.Dubininskaya 39",
                2450L
        );
        Mockito.when(paymentService.addOrder(orderDto, 1L)).thenReturn(Optional.of(newOrder));
        mvc.perform(
                        post("/order")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"description\":\"Order #342\",\"departureAddress\":\"Moscow, st.Taganskaya 150\"," +
                                                "\"destinationAddress\":\"Moscow, st.Dubininskaya 39\",\"cost\":2450}"
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    public void updateOrderStatus() throws Exception {
        StatusDto statusDto = new StatusDto(OrderStatus.PAID);
        doNothing().when(paymentService).updateOrderStatus(1L, statusDto);
        mvc.perform(
                        patch("/order/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("{\"status\": \"PAID\"}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void updateOrderStatusWithError() throws Exception {
        StatusDto statusDto = new StatusDto(OrderStatus.PAID);
        doThrow(new RuntimeException()).when(paymentService).updateOrderStatus(1L, statusDto);
        mvc.perform(
                        patch("/order/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("{\"status\": \"PAID\"}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotModified());
    }

}
