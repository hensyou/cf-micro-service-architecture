package com.cloudnativecoffee.order.model;


import java.io.Serializable;

public class Product implements Serializable{

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
