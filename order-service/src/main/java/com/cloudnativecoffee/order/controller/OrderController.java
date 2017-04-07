package com.cloudnativecoffee.order.controller;


import com.cloudnativecoffee.model.Order;
import com.cloudnativecoffee.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@RestController
@RequestMapping("/v1")
@Slf4j
public class OrderController {
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
        log.info("Order placed : "+ order.toString());
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
