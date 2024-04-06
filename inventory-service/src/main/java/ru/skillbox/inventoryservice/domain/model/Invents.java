package ru.skillbox.inventoryservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Entity
@Getter
@Setter
public class Invents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long orderId;

    @JsonIgnore
    @OneToMany(
            mappedBy = "invents",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<InventsProduct> products = new ArrayList<>();

    public Invents(long orderId) {
        this.orderId = orderId;
    }

    public void addProducts(List<InventsProduct> productsFromInput) {
        List<InventsProduct> productDetailList = new ArrayList<>();
        productsFromInput.forEach(product ->
                productDetailList.add(new InventsProduct(null, product.getProductId(),
                        product.getCount(), this)));

        getProducts().addAll(productDetailList);
    }

}
