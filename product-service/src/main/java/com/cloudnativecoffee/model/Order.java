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

    public static class Builder {
        private Order order;

        public Builder() {
            order = new Order();
        }

        public Builder userName(String userName) {
            order.setUserName(userName);
            return this;
        }

        public Builder questions(List<Product> productList) {
            order.setProductList(productList);
            return this;
        }

        public Builder orderID(String orderID) {
            order.setOrderID(orderID);
            return this;
        }

        public Builder fulfilled(Boolean fulfilled) {
            order.setFulfilled(fulfilled);
            return this;
        }

        public Order build() {
            return order;
        }
    }

}
