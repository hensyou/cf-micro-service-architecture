package com.cloudnativecoffee.model;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter @Setter
    private String userName;

    @Getter @Setter
    private List<Product> productList = new ArrayList<Product>();

    @Setter @Getter
    private String orderID;

    @Getter @Setter
    private Boolean fulfilled;

}
