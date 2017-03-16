package com.cloudnativecoffee.product.model;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Order {
	
    private String userName;
    private List<Product> productList;
    private String orderID;
    private boolean fulfilled;


}
