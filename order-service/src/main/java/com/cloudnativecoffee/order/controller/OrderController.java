package com.cloudnativecoffee.order.controller;


import com.cloudnativecoffee.model.Order;
import com.cloudnativecoffee.order.service.OrderService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class OrderController {
    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    @GetMapping("/order")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orderList = this.orderService.getAllOrders();
        if(orderList != null) {
            LOG.info("All orders returned");
            return ResponseEntity.ok(orderList);
        }
        LOG.info("No ordered found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/order/{userName}")
    public ResponseEntity<List<Order>> getAllOrderForUser(@PathVariable String userName) {
        List<Order> orderList = this.orderService.getAllOrderForUser(userName);
        if(orderList != null) {
            LOG.info("Orders for user returned "+userName);
            return ResponseEntity.ok(orderList);
        }
        LOG.info("No ordered found for user "+userName);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(@RequestBody @Valid Order order) {
        try {
            return ResponseEntity.ok(this.orderService.createOrder(order));
        } catch(RestClientException | DataIntegrityViolationException | ConstraintViolationException e) {
            LOG.error("error thrown during creation of order", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable String orderId) {
        if(this.orderService.deleteOrder(orderId))
            return ResponseEntity.ok("Order deleted successfully");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order does not exist");
    }

}
