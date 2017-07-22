package com.cloudnativecoffee.order.messaging;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.log4j.Logger;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;

import com.cloudnativecoffee.model.Order;
import com.cloudnativecoffee.order.repository.OrderRepo;

@MessageEndpoint
public class OrderMessageListener {
	private final Logger logger = Logger.getLogger(OrderMessageListener.class);
	private final OrderRepo orderRepo;

	public OrderMessageListener(OrderRepo orderRepo) {
		this.orderRepo = checkNotNull(orderRepo);
	}
	
    @StreamListener(value = "confirmationChannel")
    public void checkOrderConfirmation(Order order) {
    	logger.info("order confirmation saved, status is "+order.getFulfilled());
    	orderRepo.save(order);
    }

}
