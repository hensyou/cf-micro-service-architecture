package com.cloudnativecoffee.order.messaging;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import com.cloudnativecoffee.model.Order;

@MessagingGateway
public interface OrderMessageWriter {

    @Gateway(requestChannel = "orderChannel")
    void write(Order order);

}
