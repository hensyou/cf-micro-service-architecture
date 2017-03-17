package com.cloudnativecoffee.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.annotation.IntegrationComponentScan;

import com.cloudnativecoffee.order.messaging.OrderChannels;

@SpringBootApplication
@EnableBinding(OrderChannels.class)
@IntegrationComponentScan
@EntityScan("com.cloudnativecoffee.model")
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}
}
