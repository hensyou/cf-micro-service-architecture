/**
 * 
 */
package com.cloudnativecoffee.product.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.cloudnativecoffee.model.Product;
import com.cloudnativecoffee.product.service.ProductService;

/**
 * @author lshannon
 *
 */
@RestController
@RequestMapping("/v1")
public class ProductController {
	private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	private ProductService productService;
	

	@GetMapping("/products")
	ResponseEntity<List<Product>> products() {
		List<Product> returnValue = this.productService.getAllProducts();
		if (returnValue != null) {
			LOG.info("All products returned");
			return ResponseEntity.ok(returnValue);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping("/products")
	ResponseEntity<Product> createProducts(@RequestBody @Valid Product product) {
		try {
			return ResponseEntity.ok(this.productService.createUpdateProduct(product));
		} catch(RestClientException | DataIntegrityViolationException | ConstraintViolationException e) {
			LOG.error("error thrown during create/update of product", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/products/{productId}")
	ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
		if(this.productService.deleteProduct(productId))
			return ResponseEntity.ok("Product deleted successfully");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product does not exist");
	}

}
