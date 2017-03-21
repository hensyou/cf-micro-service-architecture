package com.cloudnativecoffee.product.messaging;


import com.cloudnativecoffee.model.Order;
import com.cloudnativecoffee.model.Product;
import com.cloudnativecoffee.product.repo.ProductRepo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ProductMessageListenerTest {

    private ProductMessageListener productMessageListener;
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
        return new Product.Builder()
                .id(new Long(123))
                .description("dummy-product-desc")
                .name("dummy-product-name")
                .price(2.5)
                .quantity(quantity).build();

    }

    private Order dummyOrderObjectCreate(int quantity) {
        return new Order.Builder()
                .orderID("dummy-order-id")
                .fulfilled(false)
                .userName("dummy-user-name")
                .questions(Arrays.asList(dummyProductObjectCreate(quantity))).build();
    }

}
