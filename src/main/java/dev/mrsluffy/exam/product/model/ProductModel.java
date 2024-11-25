package dev.mrsluffy.exam.product.model;

import dev.mrsluffy.exam.product.data.entities.enums.ProductType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {

    private String name;
    private String description;
    private ProductType type;
    private int quantity;
    private double price;
    private String currency;
    private String requirements;
}
