package com.cloudnativecoffee.order.service.impl;


import com.cloudnativecoffee.model.Order;
import com.cloudnativecoffee.order.messaging.OrderMessageWriter;
import com.cloudnativecoffee.order.repository.OrderRepo;
import com.cloudnativecoffee.order.service.OrderService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orderRepo;
    private final OrderMessageWriter orderMessageWriter;

    public OrderServiceImpl(OrderRepo orderRepo, OrderMessageWriter orderMessageWriter) {
        this.orderRepo = checkNotNull(orderRepo);
        this.orderMessageWriter = orderMessageWriter;
    }

    @Override
    public List<Order> getAllOrders() {
        return Lists.newArrayList(orderRepo.findAll());
    }

    @Override
    public List<Order> getAllOrderForUser(String userName) {
        return orderRepo.findByUserName(userName);
    }

    @Override
    public Order createOrder(Order order) {
        try {
            order.setOrderId(UUID.randomUUID().toString());
            orderMessageWriter.write(order);
            return orderRepo.save(order);
        } catch (DataIntegrityViolationException e) {
            log.error("redis save threw an error", e);
            return new Order();
        }
    }

    @Override
    public boolean deleteOrder(String orderId) {
        try {
            orderRepo.delete(orderId);
            return true;
        } catch (IllegalArgumentException e) {
            log.error("delete threw an error", e);
            return false;
        }
    }

    @Override
    public void deleteAll() {
        orderRepo.deleteAll();
    }
}
