package com.cloudnativecoffee.product.messaging;

import com.cloudnativecoffee.model.Order;
import com.cloudnativecoffee.model.Product;
import com.cloudnativecoffee.product.repo.ProductRepo;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@MessageEndpoint
public class ProductMessageListener {
    private final ProductRepo productRepo;

    public ProductMessageListener(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @StreamListener(value = "orderChannel")
	@SendTo("confirmationChannel")
    @Transactional
    public Order process(Order order) {
		List<Product> completedOrder = new ArrayList<Product>();
		int itemsFulfilled = 0;
		int itemsToFulfill = order.getProductList().size();
		for (Product orderedProduct : order.getProductList()) {
			//get orderedProduct from repo
			Product totalProduct = productRepo.findOne(orderedProduct.getId());
			if (totalProduct.getQuantity() >= orderedProduct.getQuantity()) {
				orderedProduct.setQuantity(totalProduct.getQuantity()-orderedProduct.getQuantity());
				completedOrder.add(orderedProduct);
				itemsFulfilled++;
			}
		}
		if (itemsFulfilled == itemsToFulfill) {
			productRepo.save(completedOrder);
			order.setFulfilled(true);
		}
		else {
			order.setFulfilled(false);
		}
		return order;
    }

}
