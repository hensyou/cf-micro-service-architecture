package com.cloudnativecoffee.market.model;

public class Product {
	
private Long id;
	
	private String name;
	private Double price;
	private String description;
	
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
	
	public static class Builder {
		private Product product;
		
		public Builder() {
			product = new Product();
		}
		
		public Builder id(Long id) {
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
		
		public Product build() {
			return product;
		}
	}
	
	

}
