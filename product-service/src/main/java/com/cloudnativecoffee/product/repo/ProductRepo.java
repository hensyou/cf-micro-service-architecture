/**
 * 
 */
package com.cloudnativecoffee.product.repo;

import com.cloudnativecoffee.model.Product;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lshannon
 *
 */
@Repository
@ComponentScan("com.cloudnativecoffee.model")
public interface ProductRepo extends PagingAndSortingRepository<Product, Long> {
    Product findByName(String Name);
}
