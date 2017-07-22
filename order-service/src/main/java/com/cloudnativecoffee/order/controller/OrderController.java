package com.cloudnativecoffee.order.controller;


import static com.google.common.base.Preconditions.checkNotNull;
import java.util.List;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudnativecoffee.model.Order;
import com.cloudnativecoffee.order.service.OrderService;
import com.cloudnativecoffee.product.ResourceServerConfig;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1")
public class OrderController {
	private static final Logger logger = Logger.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = checkNotNull(orderService);
    }

    @GetMapping(value = "/order" ,
            produces = "application/json")
    public ResponseEntity<List<Order>> listAllOrders() {
        return ResponseEntity.ok(this.orderService.listAllOrders());
    }

    @GetMapping(value = "/order/{userName}" ,
            produces = "application/json")
    public ResponseEntity<List<Order>> listAllOrdersForUser(@PathVariable String userName) {
    		return ResponseEntity.ok(this.orderService.listAllOrdersForUser(userName));    
    }

    @PostMapping(value = "/order",
            consumes =  "application/json",
            produces = "application/json")
    public ResponseEntity<Order> createOrder(@RequestBody @Valid Order order) {
    	logger.info("Order placed : "+ order.toString());
        return ResponseEntity.ok(this.orderService.createOrder(order));
    }

    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<String> deleteOrderForUser(@PathVariable String orderId) {
        this.orderService.deleteOrder(orderId);
            return ResponseEntity.ok("Order deleted successfully");
    }

    @DeleteMapping("/order")
    public ResponseEntity<String> deleteAllOrders() {
        this.orderService.deleteAll();
        return ResponseEntity.ok("All orders deleted");
    }

}
