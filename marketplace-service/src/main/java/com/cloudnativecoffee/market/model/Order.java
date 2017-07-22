package com.cloudnativecoffee.market.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private String userName;
    private List<Product> productList = new ArrayList<Product>();
    private String orderID;
    private Boolean fulfilled;
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
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public Boolean getFulfilled() {
		return fulfilled;
	}
	public void setFulfilled(Boolean fulfilled) {
		this.fulfilled = fulfilled;
	}
}
