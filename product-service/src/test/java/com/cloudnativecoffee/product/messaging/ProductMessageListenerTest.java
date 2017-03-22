package com.cloudnativecoffee.product.messaging;


import com.cloudnativecoffee.model.Order;
import com.cloudnativecoffee.model.Product;
import com.cloudnativecoffee.product.repo.ProductRepo;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ProductMessageListenerTest {

    private ProductMessageListener productMessageListener;
    private final String orderId = UUID.randomUUID().toString();
    @Mock
    private ProductRepo productRepo;
    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        this.productMessageListener = new ProductMessageListener(productRepo);
    }

    @Test
    public void checkProductRepoCalled() {
        when(productRepo.findOne(any())).thenReturn(dummyProductObjectCreate(10));
        Order order = productMessageListener.process(dummyOrderObjectCreate(1));
        verify(this.productRepo, times(1)).findOne(any());
        assertEquals(true, order.getFulfilled());
    }

    @Test
    public void checkFulfilledFail() {
        when(productRepo.findOne(any())).thenReturn(dummyProductObjectCreate(2));
        Order order = productMessageListener.process(dummyOrderObjectCreate(3));
        verify(this.productRepo, times(1)).findOne(any());
        assertEquals(false, order.getFulfilled());
    }

    private Product dummyProductObjectCreate(int quantity) {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        Product product = enhancedRandom.nextObject(Product.class);
        product.setQuantity(quantity);
        return product;
    }

    private Order dummyOrderObjectCreate(int quantity) {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        Order order = enhancedRandom.nextObject(Order.class);
        order.setProductList(Arrays.asList(dummyProductObjectCreate(quantity)));
        return order;
    }

}
