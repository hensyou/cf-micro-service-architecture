package com.cloudnativecoffee.order.model;


import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash("Order")
@Data
@NoArgsConstructor
public class Order {

    @Indexed
    private String userName;
    
    private List<Product> productList;
    
    @Id 
    private String orderID;

}
