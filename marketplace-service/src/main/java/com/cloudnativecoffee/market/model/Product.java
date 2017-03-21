package com.cloudnativecoffee.market.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

	private Long id;
	private String name;
	private Double price;
	private String description;
	private Integer quantity;

}
