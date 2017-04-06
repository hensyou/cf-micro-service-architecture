package com.cloudnativecoffee.order.service;


import com.cloudnativecoffee.model.Order;

import java.util.List;

public interface OrderService {

    List<Order> listAllOrders();
    List<Order> listAllOrdersForUser(String userName);
    Order createOrder(Order order);
    void deleteOrder(String orderId);
    void deleteAll();
}
