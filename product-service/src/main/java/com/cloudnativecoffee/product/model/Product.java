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

/**
 * @author lshannon
 *
 */
@Entity
@Table(name="product")
public class Product implements Serializable {
	
	private static final long serialVersionUID = -9053610858795180871L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	private Double price;
	private String description;
	
	public Product() { }
	
	public Product(String name, Double price, String description) {
		this.name = name;
		this.price = price;
		this.description = description;
	}
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
	
	

}
