package com.cloudnativecoffee.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.annotation.IntegrationComponentScan;

import com.cloudnativecoffee.product.messaging.ProductChannels;

@SpringBootApplication
@EnableBinding(ProductChannels.class)
@IntegrationComponentScan
@EntityScan("com.cloudnativecoffee.model")
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
	
}
