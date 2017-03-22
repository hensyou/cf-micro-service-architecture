package com.cloudnativecoffee.product.service;

import com.cloudnativecoffee.model.Product;
import com.cloudnativecoffee.product.repo.ProductRepo;
import com.cloudnativecoffee.product.service.impl.ProductServiceImpl;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    private ProductService productService;
    @Mock
    ProductRepo productRepo;
    @Captor
    private ArgumentCaptor<Product> productCaptor;
    @Captor
    private ArgumentCaptor<Long> productIdCapture;

    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        this.productService = new ProductServiceImpl(productRepo);
    }

    @Test
    public void verifyFindAllInvoked() {
        when(productRepo.findAll()).thenReturn(new ArrayList<Product>());
        productService.getAllProducts();
        verify(this.productRepo, times(1)).findAll();
    }

    @Test
    public void verifyRepoSaveInvoked() {
        productService.createUpdateProduct(dummyProductObjectCreate(10));
        verify(this.productRepo, times(1)).save(productCaptor.capture());
        Product product = productCaptor.getValue();
        assertEquals("dummy-product-name", product.getName());
    }

    @Test
    public void verifyRepoDelete() {
        productService.deleteProduct(new Long(123));
        verify(this.productRepo, times(1)).delete(productIdCapture.capture());
        assertEquals(new Long(123), productIdCapture.getValue());
    }

    private Product dummyProductObjectCreate(int quantity) {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        Product product = enhancedRandom.nextObject(Product.class);
        product.setQuantity(quantity);
        product.setName("dummy-product-name");
        return product;
    }

}
