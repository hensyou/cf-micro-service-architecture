package com.cloudnativecoffee.order.service.impl;


import com.cloudnativecoffee.order.model.Order;
import com.cloudnativecoffee.order.repository.OrderRepo;
import com.cloudnativecoffee.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService{
    private static Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepo orderRepo;

    @Autowired
    public OrderServiceImpl(OrderRepo orderRepo) {
        this.orderRepo = checkNotNull(orderRepo);
    }

    @Override
    public Iterable<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public List<Order> getAllOrderForUser(String userName) {
        return orderRepo.findByUserName(userName);
    }

    @Override
    public Order createOrder(Order order) {
        try {
            order.setOrderID(UUID.randomUUID().toString());
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
