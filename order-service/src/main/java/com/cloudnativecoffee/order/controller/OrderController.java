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
        List<Order> orderList = this.orderService.getAllOrders();
        if(orderList != null) {
            log.info("All orders returned");
            return ResponseEntity.ok(orderList);
        }
        log.info("No ordered found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/order/{userName}" ,
            produces = "application/json")
    public ResponseEntity<List<Order>> listAllOrdersForUser(@PathVariable String userName) {
        List<Order> orderList = this.orderService.getAllOrderForUser(userName);
        if(orderList != null) {
            log.info("Orders for user returned "+userName);
            return ResponseEntity.ok(orderList);
        }
        log.info("No ordered found for user "+userName);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/order",
            consumes =  "application/json",
            produces = "application/json")
    public ResponseEntity<Order> createOrder(@RequestBody @Valid Order order) {
        try {
            log.info("Order placed : "+ order.toString());
            return ResponseEntity.ok(this.orderService.createOrder(order));
        } catch(Exception e) {
            log.error("error thrown during creation of order", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<String> deleteOrderForUser(@PathVariable String orderId) {
        if(this.orderService.deleteOrder(orderId))
            return ResponseEntity.ok("Order deleted successfully");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order does not exist");
    }

    @DeleteMapping("/order")
    public ResponseEntity<String> deleteAllOrders() {
        this.orderService.deleteAll();
        return ResponseEntity.ok("All orders deleted");
    }

}
