package com.cloudnativecoffee.order.messaging;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;

import com.cloudnativecoffee.model.Order;
import com.cloudnativecoffee.order.repository.OrderRepo;

@MessageEndpoint
public class OrderMessageListener {
	private final OrderRepo orderRepo;

	public OrderMessageListener(OrderRepo orderRepo) {
		this.orderRepo = orderRepo;
	}
	
    @StreamListener(value = "confirmationChannel")
    public void checkOrderConfirmation(Order order) {
    	orderRepo.save(order);
    }

}
