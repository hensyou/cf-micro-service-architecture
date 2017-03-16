/**
 * 
 */
package com.cloudnativecoffee.product.repo;

import com.cloudnativecoffee.product.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lshannon
 *
 */
@Repository
public interface ProductRepo extends PagingAndSortingRepository<Product, Long> {
}
