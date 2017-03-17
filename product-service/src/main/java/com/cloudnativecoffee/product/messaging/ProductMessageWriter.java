package com.cloudnativecoffee.product.messaging;

import com.cloudnativecoffee.model.Order;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface ProductMessageWriter {

    @Gateway(requestChannel = "confirmationChannel")
    void write(Order order);
    
}
