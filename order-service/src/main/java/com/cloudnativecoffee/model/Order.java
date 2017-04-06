package com.cloudnativecoffee.model;


import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RedisHash(value = "Order", timeToLive=600)
//lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    private String orderId;

    @Indexed
    @NotEmpty
    private String userName;

    @NotEmpty
    private List<Product> productList = new ArrayList<>();

    private Boolean fulfilled;

}
