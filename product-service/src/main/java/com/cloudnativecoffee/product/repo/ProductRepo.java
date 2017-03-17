/**
 * 
 */
package com.cloudnativecoffee.product.repo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cloudnativecoffee.model.Product;

/**
 * @author lshannon
 *
 */
@Repository
@ComponentScan("com.cloudnativecoffee.model")
public interface ProductRepo extends PagingAndSortingRepository<Product, Long> {
}
