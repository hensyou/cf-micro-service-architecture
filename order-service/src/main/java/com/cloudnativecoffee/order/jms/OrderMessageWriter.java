package com.cloudnativecoffee.order.jms;

import com.cloudnativecoffee.order.model.Product;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.util.List;

@MessagingGateway
public interface OrderMessageWriter {

    @Gateway(requestChannel = "product-send")
    void write(List<Product> productList);

}
