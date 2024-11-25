package dev.mrsluffy.exam.product.data.entities;

import dev.mrsluffy.exam.helper.BaseEntity;
import dev.mrsluffy.exam.product.data.entities.enums.ProductType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * product
 *
 * @author John Andrew Camu <werdna.jac@gmail.com>
 * @version 1.0
 * @since 10/11/2024
 **/
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity {

    private String name;
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductType type;

    private int quantity;

    private double price;

    private String currency;

    private String requirements;
}