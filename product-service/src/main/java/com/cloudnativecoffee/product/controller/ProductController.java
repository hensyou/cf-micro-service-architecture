/**
 * 
 */
package com.cloudnativecoffee.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudnativecoffee.product.model.Product;
import com.cloudnativecoffee.product.repo.ProductRepo;

/**
 * @author lshannon
 *
 */
@RestController
@RequestMapping("/v1")
public class ProductController {
	
	private ProductRepo productRepo;
	
	public ProductController(ProductRepo productRepo) {
		this.productRepo = productRepo;
	}
	
	@RequestMapping("/products")
	ResponseEntity<Iterable<Product>> products() {
		Iterable<Product> returnValue = productRepo.findAll();
		if (returnValue != null) {
			return ResponseEntity.ok(returnValue);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
