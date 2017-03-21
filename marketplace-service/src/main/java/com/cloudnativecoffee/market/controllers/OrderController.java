package com.cloudnativecoffee.market.controllers;

import com.cloudnativecoffee.market.model.Order;
import com.cloudnativecoffee.market.model.Product;
import com.cloudnativecoffee.market.services.OrderService;
import com.cloudnativecoffee.market.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class OrderController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@RequestMapping("/orders")
	public String orders(Model model) {
		LOGGER.info("Entered orders");
		ResponseEntity<List<Order>> orderList = orderService.getAllOrders();
		model.addAttribute("orderList", orderList.getBody());
		return "orders";
	}

}
