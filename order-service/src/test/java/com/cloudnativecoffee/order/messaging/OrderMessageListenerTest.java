package com.cloudnativecoffee.order.messaging;

import com.cloudnativecoffee.model.Order;
import com.cloudnativecoffee.model.Product;
import com.cloudnativecoffee.order.repository.OrderRepo;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OrderMessageListenerTest {
    private OrderMessageListener orderMessageListener;
    private final String orderId = UUID.randomUUID().toString();

    @Mock
    private OrderRepo orderRepo;
    @Captor
    private ArgumentCaptor<Order> orderArgumentCaptor;
    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        this.orderMessageListener = new OrderMessageListener(orderRepo);
    }

    @Test
    public void checkOrderRepoCalled() {
        orderMessageListener.checkOrderConfirmation(dummyOrderObjectCreate());
        verify(this.orderRepo, times(1)).save(orderArgumentCaptor.capture());
        assertEquals(orderId, orderArgumentCaptor.getValue().getOrderID());
    }

    private Order dummyOrderObjectCreate() {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        Order order = enhancedRandom.nextObject(Order.class);
        order.setOrderID(orderId);
        return order;
    }

}
