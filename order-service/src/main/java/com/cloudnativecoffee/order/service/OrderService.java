package com.cloudnativecoffee.order.service;


import com.cloudnativecoffee.model.Order;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();
    List<Order> getAllOrderForUser(String userName);
    Order createOrder(Order order);
    boolean deleteOrder(String orderId);
}
