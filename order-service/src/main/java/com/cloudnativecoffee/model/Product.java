package com.cloudnativecoffee.model;


import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
/*
 * This is a member of the Order object persisted in the Key/Value store, it will be accessed
 * using the Order Id (Order Id is the key, Order object is the Value)
 * @author gnair lshannon
 *
 */
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
    private String name;
    private Double price;
    private String description;
    private int quantity;

    public Product(Long id, String name, Double price, String description, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
    }
}
