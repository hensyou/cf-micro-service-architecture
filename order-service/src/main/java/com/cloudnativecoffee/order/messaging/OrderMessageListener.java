package com.cloudnativecoffee.order.messaging;

import com.cloudnativecoffee.model.Order;
import com.cloudnativecoffee.order.repository.OrderRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;

import static com.google.common.base.Preconditions.checkNotNull;

@MessageEndpoint
@Slf4j
public class OrderMessageListener {
	private final OrderRepo orderRepo;

	public OrderMessageListener(OrderRepo orderRepo) {
		this.orderRepo = checkNotNull(orderRepo);
	}
	
    @StreamListener(value = "confirmationChannel")
    public void checkOrderConfirmation(Order order) {
		log.info("order confirmation saved, status is "+order.getFulfilled());
    	orderRepo.save(order);
    }

}
