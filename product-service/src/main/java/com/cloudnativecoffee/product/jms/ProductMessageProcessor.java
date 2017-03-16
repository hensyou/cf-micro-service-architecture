package com.cloudnativecoffee.product.jms;

import com.cloudnativecoffee.product.model.Product;
import com.cloudnativecoffee.product.repo.ProductRepo;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@MessageEndpoint
public class ProductMessageProcessor {
    private final ProductRepo productRepo;

    public ProductMessageProcessor(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @StreamListener(value = "product-send")
    public void process(List<Product> productList) {
        Map<Long, Product> productMap = productList.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
        productList.stream().forEach(product -> {
            product.setQuantity(product.getQuantity()-1);
        });
        productRepo.save(productList);
    }

}
