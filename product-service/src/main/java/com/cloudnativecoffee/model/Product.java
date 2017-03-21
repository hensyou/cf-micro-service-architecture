package com.cloudnativecoffee.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;
    
    @Getter @Setter
    private String name;
    
    @Getter @Setter
    private Double price;
    
    @Getter @Setter
    private String description;
    
    @Getter @Setter
    private Integer quantity;

}