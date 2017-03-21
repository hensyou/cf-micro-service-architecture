package com.cloudnativecoffee.order.controller;


import com.cloudnativecoffee.model.Order;
import com.cloudnativecoffee.order.service.OrderService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class OrderController {
	
    private final OrderService orderService;

    @GetMapping("/order")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(this.orderService.getAllOrders());
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
