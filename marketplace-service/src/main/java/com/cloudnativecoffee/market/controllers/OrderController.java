package com.cloudnativecoffee.market.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloudnativecoffee.market.model.Order;
import com.cloudnativecoffee.market.model.Product;
import com.cloudnativecoffee.market.services.OrderService;

@Controller
/**
 * Controller for the Order page HTML
 * @author lshannon
 *
 */
public class OrderController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@RequestMapping("/orders")
	/**
	 * Controller to view the orders in the system
	 * @param model
	 * @return
	 */
	public String orders(Model model) {
		LOGGER.info("Entered orders");
		ResponseEntity<List<Order>> orderList = orderService.getAllOrders();
		model.addAttribute("orderList", orderList.getBody());
		LOGGER.info("Orders returned: " + orderList.getBody().toString());
		return "orders";
	}
	
	@GetMapping("/placeOrder")
	/**
	 * Controller to place an order
	 * @return
	 */
	public String placeOrder() {
		Order newOrder = new Order();
		List<Product> products = new ArrayList<Product>();
		Product product = new Product();
		product.setName("Cappuccino");
		product.setPrice(Double.valueOf("3.25"));
		product.setQuantity(1);
		product.setId(Long.valueOf("3"));
		products.add(product);
		newOrder.setProductList(products);
		newOrder.setUserName("Luke");
		orderService.placeOrder(newOrder);
		
		return "redirect:/orders";
	}
	

}
