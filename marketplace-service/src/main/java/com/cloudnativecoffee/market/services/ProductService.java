/**
 * 
 */
package com.cloudnativecoffee.market.services;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cloudnativecoffee.market.model.Product;

/**
 * @author lshannon
 *
 */
@Service
public class ProductService {
	
	private RestTemplate restTemplate;
	
	
	public ProductService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	
	
	public ResponseEntity<List<Product>> getAllProducts() {
		ParameterizedTypeReference<List<Product>> parameterizedTypeReference = new ParameterizedTypeReference<List<Product>>() {};
		ResponseEntity<List<Product>> responseEntity =  restTemplate.exchange("http://localhost:8081/v1/products", HttpMethod.GET, null, parameterizedTypeReference);

		return responseEntity;
	}
	
	
	

}
