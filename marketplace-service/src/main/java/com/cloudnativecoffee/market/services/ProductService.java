/**
 * 
 */
package com.cloudnativecoffee.market.services;

import java.util.List;

import com.cloudnativecoffee.market.model.Order;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
	private final Logger LOG = Logger.getLogger(ProductService.class);
	private static final String FALLBACK_METHOD = "genericErrorMessage";
	private final String productServiceHost;
	private final String productBaseurl;
	private RestTemplate restTemplate;

	public ProductService(@Value("${marketplace.services.product.id}") String productServiceHost,
						  @Value("${marketplace.services.product.api.url}") String productBaseurl,
						  RestTemplate restTemplate) {
		this.productServiceHost = productServiceHost;
		this.productBaseurl = productBaseurl;
		this.restTemplate = restTemplate;
	}

	@HystrixCommand(fallbackMethod = FALLBACK_METHOD,
			commandProperties=@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="2000"))
	public ResponseEntity<List<Product>> getAllProducts() {
		ParameterizedTypeReference<List<Product>> parameterizedTypeReference = new ParameterizedTypeReference<List<Product>>() {};
		String apiUrl = new StringBuilder().append(productServiceHost).append(productBaseurl).toString();
		return ResponseEntity.status(HttpStatus.OK).body(restTemplate.exchange(apiUrl, HttpMethod.GET, null, parameterizedTypeReference).getBody());
	}

	public ResponseEntity<List<Product>> genericErrorMessage(Throwable exception) {
		LOG.error("An error has occurred. Callback method called", exception);
		return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
	}
}
