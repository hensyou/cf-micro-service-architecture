package com.cloudnativecoffee.order.repository;

import com.cloudnativecoffee.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends CrudRepository<Order, String>{
    
	List<Order> findByUserName(String userName);
}
