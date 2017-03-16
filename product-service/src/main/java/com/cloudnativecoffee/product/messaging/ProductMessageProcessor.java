package com.cloudnativecoffee.product.messaging;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.transaction.annotation.Transactional;

import com.cloudnativecoffee.product.model.Order;
import com.cloudnativecoffee.product.model.Product;
import com.cloudnativecoffee.product.repo.ProductRepo;

@MessageEndpoint
public class ProductMessageProcessor {
    private final ProductRepo productRepo;

    public ProductMessageProcessor(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @StreamListener(value = "orderChannel")
    @Transactional
    public void process(Order order) {
    	List<Product> completedOrder = new ArrayList<Product>();
		int itemsFullfilled = 0;
		int itemsToFullfil = order.getProductList().size();
    	for (Product product : order.getProductList()) {
    		//get product from repo
    		Product currentProduct = productRepo.findOne(product.getId());

    		if (currentProduct.getQuantity() >= product.getQuantity()) {
    			product.setQuantity(product.getQuantity()-product.getQuantity());
    			completedOrder.add(product);
    			itemsFullfilled++;
    		}
    		
    	}
    	if (itemsFullfilled == itemsToFullfil) {
    		productRepo.save(completedOrder);
    	}
    	else {
    		
    	}
        
    }

}
