# Market Place Service

## Purpose

This service displays a unified view of the backing services for the client to interact with.

## Important Concept

1. Creating a UI Application with Spring Boot
2. Rest Template To Call Backing Servers
3. Circuit Breaker To Protect When Backing Services Are Down
4. Profiles For Different Behavior In Different Environments
5. Gradle
6. Spring Boot Concepts (Spring Boot Starters, Auto Configuration, Controllers, Services)

## Create The Service

Create the service:

```shell

curl https://start.spring.io/starter.tgz -d style=web,thymeleaf,flyway -d groupId=com.cloudnativecoffee -d name=marketplace-service | tar -xzvf -

```
Question: What did this do? Lets look at start.spring.io

Ensure your gradle dependancies look like this.

```shell

dependencies {
	compile('org.springframework.cloud:spring-cloud-starter-hystrix')
	compile('org.springframework.boot:spring-boot-starter-thymeleaf')
	compile('org.springframework.boot:spring-boot-starter-web')
	compileOnly('org.projectlombok:lombok:1.16.14')
	compile group: 'org.springframework.cloud', name: 'spring-cloud-starter-hystrix', version: '1.2.6.RELEASE'

	testCompile('org.springframework.boot:spring-boot-starter-test')
}

```

## Review The Domain Objects

We want to keep our definitions are streamline as possible. Lombok helps with this.

```java

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private String userName;
    private List<Product> productList = new ArrayList<Product>();
    private String orderID;
    private Boolean fulfilled;
}

```

With this approach its easier to define object in different services. Why not have a common library of code with models that all applications share?

## Reviewing The Controllers

Controllers are used to route requests, Services are used to perform business operations (ie: calling backing services). Lets review

```java

@Controller
public class ProductController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
	private final ProductService productService;
	
	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@RequestMapping("/products")
	public String products(Model model) {
		LOGGER.info("Entered products");
		ResponseEntity<List<Product>> productList = productService.getAllProducts();
		LOGGER.info("testing list"+ productList.toString());
		model.addAttribute("productList", productList.getBody());
		return "products";
	}
}

```
Important concepts are:

- @Controller
- @RequestMapping
- Model

Question: Why use an args constructor? Why not autowire what we need into the Controller?

Before going on to the services lets take a look at the Product Service.

## What is Thymeleaf?

Thymeleaf is a modern server-side Java template engine for both web and standalone environments. Thymeleaf's main goal is to bring elegant natural templates to your development workflow — HTML that can be correctly displayed in browsers and also work as static prototypes, allowing for stronger collaboration in development teams. It has great support for Spring:

http://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html

Lets explore Thymeleaf's in the working Spring Boot sample.

There is support for templates to centralize common code. We can also see how it can be used to display informtation passed from a backing controller.

```html

<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Getting Started: Serving Web Content</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>

	<div th:replace="fragments/menu :: menu"></div>
	
    <p>Product Page</p>
  	<div th:if="${productList != null}">
	    <h1>List of Products:</h1>
	    <ul>
	       <li th:each="product : ${productList}" th:text="${product.name}">test-product</li>
	    </ul>
	</div>
</body>
</html>

```

Question: Why not display HTML or Javascript on it own? PCF has a static build pack. Why put this in Spring Boot?

## Review The Services


## Create The Fallback

## Update The The UI

Update the UI

## Switch To Create The Order Service

Come back once that is completed

## Add Services

Add the services to call the Order Service

## Update The UI

Update The UI

## Added Security

Login for the market place

## Review The Authentication Server

## Discuss JWT 



