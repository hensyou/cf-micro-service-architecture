package com.cloudnativecoffee.product.messaging;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import com.cloudnativecoffee.product.model.Order;

@MessagingGateway
public interface ProductMessageWriter {

    @Gateway(requestChannel = "confirmationChannel")
    void write(Order order);
    
}
