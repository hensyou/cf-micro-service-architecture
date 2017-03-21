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
