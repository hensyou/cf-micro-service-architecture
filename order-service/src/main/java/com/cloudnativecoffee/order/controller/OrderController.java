package com.cloudnativecoffee.order.controller;


import com.cloudnativecoffee.order.model.Order;
import com.cloudnativecoffee.order.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
public class OrderController {
    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/order")
    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/order/{userName}")
    public List<Order> getAllOrderForUser(@PathVariable String userName) {
        return orderRepository.findByUserName(userName);
    }

    @PostMapping("/order")
    public Order createOrder(@RequestBody @Valid Order order) {
        order.setOrderID(UUID.randomUUID().toString());
        order = orderRepository.save(order);
        return order;
    }

}
