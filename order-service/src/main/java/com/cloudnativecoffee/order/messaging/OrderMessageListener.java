package com.cloudnativecoffee.order.messaging;

import com.cloudnativecoffee.model.Order;
import com.cloudnativecoffee.order.repository.OrderRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;

@MessageEndpoint
public class OrderMessageListener {
	private static final Logger LOG = LoggerFactory.getLogger(OrderMessageListener.class);
	private final OrderRepo orderRepo;

	public OrderMessageListener(OrderRepo orderRepo) {
		this.orderRepo = orderRepo;
	}
	
    @StreamListener(value = "confirmationChannel")
    public void checkOrderConfirmation(Order order) {
		LOG.info("order confirmation saved, status is "+order.getFulfilled());
    	orderRepo.save(order);
    }

}
