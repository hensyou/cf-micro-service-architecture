/**
 * 
 */
package com.cloudnativecoffee.product.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author lshannon
 *
 */
@Entity
@Table(name="product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
	
	private static final long serialVersionUID = -9053610858795180871L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;

	private String name;
	private Double price;
	private String description;
	private Integer quantity;

    public Product(String name, Double price, String description, Integer quantity) {
		this.name = name;
		this.price = price;
		this.description = description;
		this.quantity = quantity;
	}

}
