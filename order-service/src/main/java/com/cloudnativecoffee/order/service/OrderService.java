package com.cloudnativecoffee.order.service;


import com.cloudnativecoffee.order.model.Order;

import java.util.List;

public interface OrderService {

    Iterable<Order> getAllOrders();
    List<Order> getAllOrderForUser(String userName);
    Order createOrder(Order order);
    boolean deleteOrder(String orderId);
}
