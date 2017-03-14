package com.cloudnativecoffee.market.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloudnativecoffee.market.model.Product;
import com.cloudnativecoffee.market.services.ProductService;

@Controller
public class ProductController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
	private final ProductService productService;
	
	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@RequestMapping("/products")
	public String products(Model model) {
		LOGGER.info("Entered products");
		ResponseEntity<List<Product>> productList = productService.getAllProducts();
		LOGGER.info("testing list"+ productList.toString());
		model.addAttribute("productList", productList.getBody());
		return "products";
	}
}
