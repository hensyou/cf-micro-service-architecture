package com.cloudnativecoffee.product.service;

import com.cloudnativecoffee.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product createUpdateProduct(Product product);

    boolean deleteProduct(Long productId);
}
