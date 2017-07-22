package com.cloudnativecoffee.model;

import java.io.Serializable;


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
    private Integer quantity;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
