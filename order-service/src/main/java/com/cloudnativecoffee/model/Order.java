package com.cloudnativecoffee.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash("Order")
@Data
@NoArgsConstructor
public class Order implements Serializable{

    private static final long serialVersionUID = 1L;

    @Indexed
    private String userName;
    
    private List<Product> productList = new ArrayList<Product>();
    
    @Id @Setter @Getter
    private String orderID;
    
    private Boolean fulfilled;

}
