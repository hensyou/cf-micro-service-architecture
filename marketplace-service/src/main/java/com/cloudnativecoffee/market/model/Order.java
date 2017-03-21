package com.cloudnativecoffee.market.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private String userName;
    private List<Product> productList = new ArrayList<Product>();
    private String orderID;
    private Boolean fulfilled;
}
