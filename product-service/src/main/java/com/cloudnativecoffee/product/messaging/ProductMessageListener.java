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
			order.setFulfilled(true);
		}
		else {
			order.setFulfilled(false);
		}
		return order;
    }

}
