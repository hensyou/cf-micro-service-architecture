package com.cloudnativecoffee.product.service.impl;

import com.cloudnativecoffee.model.Product;
import com.cloudnativecoffee.product.repo.ProductRepo;
import com.cloudnativecoffee.product.service.ProductService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);
    private ProductRepo productRepo;

    @Override
    public List<Product> getAllProducts() {
        LOG.info("retrieving all products");
        return Lists.newArrayList(productRepo.findAll());
    }

    @Override
    public Product createUpdateProduct(Product product) throws ConstraintViolationException {
        Product productFromDB = productRepo.findByName(product.getName());
        if(productFromDB != null )
            product.setId(productFromDB.getId());
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
