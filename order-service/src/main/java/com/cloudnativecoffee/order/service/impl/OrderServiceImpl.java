package com.cloudnativecoffee.order.service.impl;


import com.cloudnativecoffee.order.messaging.OrderMessageWriter;
import com.cloudnativecoffee.model.Order;
import com.cloudnativecoffee.order.repository.OrderRepo;
import com.cloudnativecoffee.order.service.OrderService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepo orderRepo;
    private final OrderMessageWriter orderMessageWriter;

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
            order.setOrderID(UUID.randomUUID().toString());
            orderMessageWriter.write(order);
            return orderRepo.save(order);
        } catch (DataIntegrityViolationException e) {
            LOG.error("redis save threw an error", e);
            return new Order();
        }
    }

    @Override
    public boolean deleteOrder(String orderId) {
        try {
            orderRepo.delete(orderId);
            return true;
        } catch (IllegalArgumentException e) {
            LOG.error("delete threw an error", e);
            return false;
        }
    }
}
