package com.cloudnativecoffee.product;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cloudnativecoffee.product.model.Product;
import com.cloudnativecoffee.product.repo.ProductRepo;

@SpringBootApplication
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner init(ProductRepo repo) {
		return (args) -> {
			repo.save(new Product("Black Coffee with sugar", Double.valueOf("2.50"), "Borrrinnnng"));
			repo.save(new Product("Coffee with milk and sugar", Double.valueOf("3.50"), "Lactose"));
		};
	}
}
