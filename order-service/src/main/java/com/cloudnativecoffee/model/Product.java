package com.cloudnativecoffee.model;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Data
@NoArgsConstructor
/*
 * This is a member of the Order object persisted in the Key/Value store, it will be accessed
 * using the Order Id (Order Id is the key, Order object is the Value)
 * @author gnair lshannon
 *
 */
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

    @Getter @Setter
    private Long id;
    private String name;
    private Double price;
    private String description;
    @Getter @Setter
    private Integer quantity;

}
