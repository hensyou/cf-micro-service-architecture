package com.cloudnativecoffee.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.core.annotation.Order;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.cloudnativecoffee.product.messaging.ProductChannels;

@SpringBootApplication
@EnableBinding(ProductChannels.class)
@IntegrationComponentScan
@EntityScan("com.cloudnativecoffee.model")
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class ProductServiceApplication extends WebSecurityConfigurerAdapter{

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
	
}
