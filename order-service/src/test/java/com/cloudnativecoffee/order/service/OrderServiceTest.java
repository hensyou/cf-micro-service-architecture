package com.cloudnativecoffee.order.service;

import com.cloudnativecoffee.model.Order;
import com.cloudnativecoffee.model.Product;
import com.cloudnativecoffee.order.messaging.OrderMessageWriter;
import com.cloudnativecoffee.order.repository.OrderRepo;
import com.cloudnativecoffee.order.service.impl.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class OrderServiceTest {
    private OrderService orderService;
    private final String orderId = UUID.randomUUID().toString();

    @Mock
    OrderRepo orderRepo;
    @Mock
    OrderMessageWriter orderMessageWriter;
    @Captor
    private ArgumentCaptor<Order> orderArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> orderIdCapture;

    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        this.orderService = new OrderServiceImpl(orderRepo, orderMessageWriter);
    }

    @Test
    public void verifyGetAllOrders() {
        when(orderRepo.findAll()).thenReturn(new ArrayList<Order>());
        orderService.getAllOrders();
        verify(this.orderRepo, times(1)).findAll();
    }

    @Test
    public void verifyRepoDuringCreate() {
        orderService.createOrder(dummyOrderObjectCreate());
        verify(orderRepo, times(1)).save(orderArgumentCaptor.capture());
        assertEquals("dummy-user-name", orderArgumentCaptor.getValue().getUserName());
    }

    @Test
    public void verifyDeleteOrder() {
        orderService.deleteOrder(orderId);
        verify(this.orderRepo, times(1)).delete(orderIdCapture.capture());
        assertEquals(orderId, orderIdCapture.getValue());

    }

    private Order dummyOrderObjectCreate() {
        return new Order.Builder()
                .orderID(orderId)
                .fulfilled(false)
                .userName("dummy-user-name")
                .questions(new ArrayList<Product>()).build();
    }

}
