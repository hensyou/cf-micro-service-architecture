package com.cloudnativecoffee.product.service.impl;

import com.cloudnativecoffee.product.model.Product;
import com.cloudnativecoffee.product.repo.ProductRepo;
import com.cloudnativecoffee.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepo productRepo;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Iterable<Product> getAllProducts() {
        LOG.info("retrieving all products");
        return productRepo.findAll();
    }

    @Override
    public Product createUpdateProduct(Product product) throws ConstraintViolationException {
        product = productRepo.save(product);
        return product;
    }

    @Override
    public boolean deleteProduct(Long productId) {
        try {
           productRepo.delete(productId);
           return true;
        } catch (EmptyResultDataAccessException e) {
            LOG.error("productId already exists", e);
            return false;
        }
    }
}
