package com.cloudnativecoffee.market.model;

import lombok.Data;

@Data
public class Product {
	
	private Long id;
	private String name;
	private Double price;
	private String description;	
	
}
