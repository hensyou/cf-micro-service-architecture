package com.cloudnativecoffee.market.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrderController {

	@RequestMapping("/orders")
	public String orders(Model model) {
		return "orders";
	}
}
