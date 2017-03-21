package com.cloudnativecoffee.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private Double price;

    @Getter @Setter
    private String description;

    @Getter @Setter
    private Integer quantity;

    public Product(String name, Double price, String description, Integer quantity) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
    }

    public static class Builder {
        private Product product;

        public Builder() {
            product = new Product();
        }

        public Builder id(long id) {
            product.setId(id);
            return this;
        }

        public Builder name(String name) {
            product.setName(name);
            return this;
        }

        public Builder price(Double price) {
            product.setPrice(price);
            return this;
        }

        public Builder description(String description) {
            product.setDescription(description);
            return this;
        }

        public Builder quantity(Integer quantity) {
            product.setQuantity(quantity);
            return this;
        }

        public Product build() {
            return product;
        }
    }
}