package com.cloudnativecoffee.model;


import lombok.*;

import java.io.Serializable;


/*
 * This is a member of the Order object persisted in the Key/Value store, it will be accessed
 * using the Order Id (Order Id is the key, Order object is the Value)
 * @author gnair lshannon
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Double price;
    private String description;
    private Integer quantity;

}
