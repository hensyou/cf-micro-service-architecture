package com.cloudnativecoffee.market.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cloudnativecoffee.market.model.Order;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class OrderService {
    private final Logger LOG = Logger.getLogger(OrderService.class);

    private static final String FALLBACK_METHOD = "genericErrorMessage";
    private static final String FALLBACK_METHOD_USER_ORDER = "genericErrorMessageUserOrder";
    private static final String FALLBACK_METHOD_ORDER_CREATE = "genericErrorMessageOrderCreate";
    private final String orderServiceHost;
    private final String orderBaseurl;
    private RestTemplate restTemplate;

    public OrderService(@Value("${marketplace.services.order.id}") String orderServiceHost,
                        @Value("${marketplace.services.order.api.url}") String orderBaseurl,
                        @Qualifier("restTemplate") RestTemplate restTemplate) {
        this.orderServiceHost = orderServiceHost;
        this.orderBaseurl = orderBaseurl;
        this.restTemplate = restTemplate;

    }
    
    public void placeOrder(Order orderToPlace) {
    		String apiUrl = new StringBuilder().append(orderServiceHost).append(orderBaseurl).toString();
    		ResponseEntity.status(HttpStatus.OK).body(restTemplate.postForEntity(apiUrl, createRequestEntity(orderToPlace), Order.class).getBody());
    }
    
    private <T>HttpEntity<T> createRequestEntity(T jsonBody) {
    		HttpHeaders httpHeaders = new HttpHeaders();
    		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    		return new HttpEntity<>(jsonBody, httpHeaders);
    }

    @HystrixCommand(
    		fallbackMethod = FALLBACK_METHOD,
    		commandProperties = {
    				@HystrixProperty(name="circuitBreaker.requestVolumeThreshold",value="5"),
    				@HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value="10000")
    		}
    		)
    
    public ResponseEntity<List<Order>> getAllOrders() {
        ParameterizedTypeReference<List<Order>> parameterizedTypeReference = new ParameterizedTypeReference<List<Order>>() {};
        String apiUrl = new StringBuilder().append(orderServiceHost).append(orderBaseurl).toString();
        return ResponseEntity.status(HttpStatus.OK).body(restTemplate.exchange(apiUrl, HttpMethod.GET, null, parameterizedTypeReference).getBody());
    }


    @HystrixCommand(fallbackMethod = FALLBACK_METHOD_USER_ORDER)
    public ResponseEntity<List<Order>> getOrdersForUser(String userName) {
        ParameterizedTypeReference<List<Order>> parameterizedTypeReference = new ParameterizedTypeReference<List<Order>>() {};
        //String apiUrl = new StringBuilder().append(orderServiceHost).append(orderBaseurl).append(userName).toString();
        return ResponseEntity.status(HttpStatus.OK).body(restTemplate.exchange("https://coffee-order-service.cfapps.io/v1/order/Luke", HttpMethod.GET, null, parameterizedTypeReference).getBody());
    }

    public ResponseEntity<List<Order>> genericErrorMessage(Throwable exception) {
        LOG.error("An error has occurred. Callback method called", exception);
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<List<Order>> genericErrorMessageUserOrder(String userName, Throwable exception) {
        LOG.error("An error has occurred. Callback method called", exception);
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<List<Order>> genericErrorMessageOrderCreate(Throwable exception) {
        LOG.error("An error has occurred. Callback method called", exception);
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

}
