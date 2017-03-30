package com.cloudnativecoffee.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RedisHash("Order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    private String orderID;

    @Indexed
    private String userName;

    private List<Product> productList = new ArrayList<Product>();

    private Boolean fulfilled;

}
