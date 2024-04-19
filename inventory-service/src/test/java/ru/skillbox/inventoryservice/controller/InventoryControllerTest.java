package ru.skillbox.inventoryservice.controller;

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
import ru.skillbox.inventoryservice.domain.dto.ProductDto;
import ru.skillbox.inventoryservice.domain.model.Product;
import ru.skillbox.inventoryservice.repository.ProductRepository;
import ru.skillbox.inventoryservice.service.InventoryService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(InventoryController.class)
class InventoryControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private InventoryService inventoryService;
    @MockBean
    private ProductRepository productRepository;

    @Configuration
    @ComponentScan(basePackageClasses = {InventoryController.class})
    public static class TestConf {
    }
    private Product product;

    private Product newProduct;

    private List<Product> products;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setProductId(1L);
        product.setCount(10);

        newProduct = new Product();
        newProduct.setProductId(2L);
        newProduct.setCount(20);

        products = Collections.singletonList(product);

    }

    @Test
    void addProducts() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setProductId(1L);
        productDto.setCount(10);


        Mockito.when(inventoryService.addProducts(Collections.singletonList(productDto))).thenReturn(products);
        mvc.perform(
                        post("/products")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        "[{\"productId\":1,\"count\":10}]"
                                )
                                .header("id", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(product.getCount()))));
    }

    @Test
    void listProducts() throws Exception {
        Mockito.when(inventoryService.getAllProducts()).thenReturn(products);
        mvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(
                        content().string(containsString(String.valueOf(product.getCount()))));
    }

    @Test
    void getProductById() throws Exception {
        Mockito.when(productRepository.findProductByProductId(1L)).thenReturn(Optional.of(product));
        mvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(
                        content().string(containsString(String.valueOf(product.getCount()))));

        mvc.perform(get("/products/2"))
                .andExpect(status().isBadRequest())
                .andExpect(
                        content().string(containsString(String.valueOf(newProduct.getProductId()))));

    }

}