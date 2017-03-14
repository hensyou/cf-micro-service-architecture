/**
 * 
 */
package com.cloudnativecoffee.product.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cloudnativecoffee.product.model.Product;

/**
 * @author lshannon
 *
 */
@Repository
public interface ProductRepo extends PagingAndSortingRepository<Product, Long> {

}
