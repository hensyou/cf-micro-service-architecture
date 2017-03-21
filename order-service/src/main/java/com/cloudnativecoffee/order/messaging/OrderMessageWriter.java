package com.cloudnativecoffee.order.messaging;

import com.cloudnativecoffee.model.Order;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface OrderMessageWriter {

    @Gateway(requestChannel = "orderChannel")
    void write(Order order);

}
