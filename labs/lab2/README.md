# Lab 2

In this lab we will cover persistence and DB migrations for the Product service

## Concepts To Learn

1. Updating a Pojo to be persisted
2. Adding and working with Flyway in Spring Boot
3. Working with Spring Data repositories
4. Runing local tests with H2
5. Connecting to DB running in your PCF space

## Prerequisites
If you wish to use Lombok for this lab, you can follow these instructions to install it:

https://projectlombok.org/download.html

## Add The Required Dependancies

```shell


// https://mvnrepository.com/artifact/com.h2database/h2
compile group: 'com.h2database', name: 'h2', version: '1.4.194'

// https://mvnrepository.com/artifact/postgresql/postgresql
compile group: 'postgresql', name: 'postgresql', version: '9.4.1208-jdbc42-atlassian-hosted'

// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa
compile group: 'org.springframework.boot', name: 'spring-boot-starter-data-jpa'


```

## Update the Pojo

Update the Pojo class to have the annotations @Entity and @Id

```java

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Product {
	
	@Id
	private Long id;
	private String name;
	
	public Product() { }
 	
	public Product(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}


```

NOTE: This would work with a Lombok class as well

## Create the Repository Class

Create a new Class that looks like the following:

```java


import org.springframework.data.repository.CrudRepository;

public interface ProductRepo extends CrudRepository<Product, Long> {

}

```






