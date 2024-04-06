package ru.skillbox.inventoryservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "invent_products")
@AllArgsConstructor
public class InventsProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Integer count;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "invents_id")
    private Invents invents;

}
