# Labs

The following are some of hands on exercises to reinforce key concepts in the Marketplace Service

## Concepts To Learn
1. Create a simple UI using Spring Boot and ThymeLeaf
2. Create a simple Product Backing Service using Spring Boot
3. Create a simple Order Backing Service using Spring Boot
4. Communicate Via Rest Between Two Services
5. Deploying To PCF
6. Trouble Shooting and Monitoring

## Building Out The Rest Services

Lets create a basic Rest Client.

```shell

curl https://start.spring.io/starter.tgz -d style=web,actuator -d groupId=com.sample -d name=product-service -d type=gradle-project | tar -xzvf -

```
If you are using start.spring.io, simply select the following projects.

![Architecture](images/project_create.png)

### Create The Model Object

Create a class called Product. Add the following attributes:

```java

public class Product {

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

Optionally this can be converted to Lombok to dramatically reduce the lines of code in the object definition. Why would this matter?

### Create the Rest Endpoint

Create a class called Product Controller.

Add the following:

```java

@RestController
@RequestMapping("/v1")
public class ProductController {
	
	@GetMapping("/product")
	public ResponseEntity<Iterable<Product>> products() {
  
		List<Product> products = new ArrayList<Product>();
		products.add(new Product(Long.valueOf(1), "Car"));
		products.add(new Product(Long.valueOf(2), "Boat"));
		products.add(new Product(Long.valueOf(3), "Real Live Dinosaur"));
		return ResponseEntity.ok(products);
	}

}


```

Start your application and test the end point at: http://localhost:8080/v1/product

### Enable Actuator

Add the following to your application.properties/application.yml

```shell

endpoints.sensitive=false

```

Restart your application and try some of the end points:

- http://localhost:8080/mappings
- http://localhost:8080/health
- http://localhost:8080/env

### Deploy to Cloud Foundry

Ensure to clean and build your Gradle project and then deploy to PWS using the cf command line or the 


## Building Out The UI Client

Lets build out a basic UI. 

```shell

curl https://start.spring.io/starter.tgz -d style=web,thymeleaf,actuator -d groupId=com.cloudnativecoffee -d name=marketplace-service -d type=gradle-project | tar -xzvf -

```

If you are using start.spring.io, simply select the following projects.

![Architecture](images/project_create.png)

Once created, import into your IDE of choice (ie: import existing Gradle project).

This project uses Thymeleaf, a very simple HTML attribute based templating engine. Lets add our basic UI. Inside of src/main/resources/templates, create a file called index.html and paste the following into it.

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Simple UI</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
	
	<p th:text="'Hello, ' + ${name} + '!'" />
    
</body>
</html>

```


## Preparing To Deploy To PCF

## Deploy To PCF

## Using RestTemplate To Communicate Between Services

## Deploy To PCF

## Adding Security To The Marketplace

## Deploying To PCF

## Troubleshooting and Monitoring
