/**
 * 
 */
package com.cloudnativecoffee.product.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lshannon
 *
 */
@Entity
@Table(name="product")
@Data
@NoArgsConstructor
public class Product implements Serializable {
	
	private static final long serialVersionUID = -9053610858795180871L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	private Double price;
	private String description;
	private Integer quantity;

	public Product(String name, Double price, String description, Integer quantity) {
		this.name = name;
		this.price = price;
		this.description = description;
		this.quantity = quantity;
	}

}
