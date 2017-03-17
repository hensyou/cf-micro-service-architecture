package com.cloudnativecoffee.order.controller;


import com.cloudnativecoffee.model.Order;
import com.cloudnativecoffee.order.service.OrderService;
import com.google.common.collect.Lists;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class OrderController {
	
    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/order")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orderList = Lists.newArrayList(this.orderService.getAllOrders());
        return ResponseEntity.ok(orderList);
    }

    @GetMapping("/order/{userName}")
    public ResponseEntity<List<Order>> getAllOrderForUser(@PathVariable String userName) {
        return ResponseEntity.ok(this.orderService.getAllOrderForUser(userName));
    }

    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(@RequestBody @Valid Order order) {
        return ResponseEntity.ok(this.orderService.createOrder(order));
    }

    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(this.orderService.deleteOrder(orderId));
    }

}
