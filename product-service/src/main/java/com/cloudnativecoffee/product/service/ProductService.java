package com.cloudnativecoffee.product.service;

import com.cloudnativecoffee.product.model.Product;

public interface ProductService {

    Iterable<Product> getAllProducts();

    Product createUpdateProduct(Product product);

    boolean deleteProduct(Long productId);
}
