package com.cloudnativecoffee.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.annotation.IntegrationComponentScan;

import com.cloudnativecoffee.order.messaging.OrderChannels;

@SpringBootApplication
@EnableBinding(OrderChannels.class)
@IntegrationComponentScan
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}
}
