package com.cloudnativecoffee.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("Order")
public class Order implements Serializable{

    private static final long serialVersionUID = 3734899149255587948L;

    @Id
    private String orderId;

    @Indexed
    @NotEmpty
    private String userName;

    @NotEmpty
    private List<Product> productList = new ArrayList<>();

    private Boolean fulfilled;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public Boolean getFulfilled() {
		return fulfilled;
	}

	public void setFulfilled(Boolean fulfilled) {
		this.fulfilled = fulfilled;
	}

}
