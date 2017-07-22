package com.cloudnativecoffee.order.service.impl;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudnativecoffee.model.Order;
import com.cloudnativecoffee.order.exception.OrderCreationException;
import com.cloudnativecoffee.order.exception.OrderDeletionException;
import com.cloudnativecoffee.order.messaging.OrderMessageWriter;
import com.cloudnativecoffee.order.repository.OrderRepo;
import com.cloudnativecoffee.order.service.OrderService;
import com.google.common.collect.Lists;

@Service
public class OrderServiceImpl implements OrderService {
	private final Logger logger = Logger.getLogger(OrderServiceImpl.class);
    private final OrderRepo orderRepo;
    private final OrderMessageWriter orderMessageWriter;

    public OrderServiceImpl(OrderRepo orderRepo, OrderMessageWriter orderMessageWriter) {
        this.orderRepo = checkNotNull(orderRepo);
        this.orderMessageWriter = orderMessageWriter;
    }

    @Override
    public List<Order> listAllOrders() {
        return Lists.newArrayList(orderRepo.findAll());
    }

    @Override
    public List<Order> listAllOrdersForUser(String userName) {
        return orderRepo.findByUserName(userName);
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        try {
            order.setOrderId(UUID.randomUUID().toString());
            order.setFulfilled(false);
            orderMessageWriter.write(order);
            return orderRepo.save(order);
        } catch (DataIntegrityViolationException e) {
        	logger.error("redis save threw an error", e);
            throw new OrderCreationException("Failed to create an order", e);
        }
    }

    @Override
    public void deleteOrder(String orderId) {
        try {
            orderRepo.delete(orderId);
        } catch (IllegalArgumentException e) {
        	logger.error("delete threw an error", e);
            throw new OrderDeletionException("Failed to delete the order ", e);
        }
    }

    @Override
    public void deleteAll() {
        orderRepo.deleteAll();
    }
}
