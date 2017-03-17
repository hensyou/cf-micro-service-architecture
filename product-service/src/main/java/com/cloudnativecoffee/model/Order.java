package com.cloudnativecoffee.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName;
    @Getter @Setter
    private List<Product> productList;
    private String orderID;
    private Boolean fulfilled;


}
