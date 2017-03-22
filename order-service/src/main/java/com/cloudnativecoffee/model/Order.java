package com.cloudnativecoffee.model;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RedisHash("Order")
@Data
@NoArgsConstructor
public class Order implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @Setter @Getter
    private String orderID;

    @Indexed
    @Setter @Getter
    private String userName;

    @Setter @Getter
    private List<Product> productList = new ArrayList<Product>();

    @Setter @Getter
    private Boolean fulfilled;

}
