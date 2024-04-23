package ru.skillbox.deliveryservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.deliveryservice.exception.DeliveryNotFoundException;
import ru.skillbox.deliveryservice.service.DeliveryService;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(DeliveryController.class)
class DeliveryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DeliveryService deliveryService;

    @Configuration
    @ComponentScan(basePackageClasses = {DeliveryController.class})
    public static class TestConf {
    }
    @Test
    void deleteDeliverByIdErrorTest() throws Exception {
        doThrow(new DeliveryNotFoundException("Delivery with ID 1 not found."))
                .when(deliveryService).deleteDeliveryById(1L);
        mvc.perform(
                        delete("/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteDeliverByIdSuccessTest() throws Exception {
        doNothing().when(deliveryService).deleteDeliveryById(1L);
        mvc.perform(
                        delete("/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }}