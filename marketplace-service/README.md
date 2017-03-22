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

## What is Thymeleaf?

Thymeleaf is a modern server-side Java template engine for both web and standalone environments. Thymeleaf's main goal is to bring elegant natural templates to your development workflow — HTML that can be correctly displayed in browsers and also work as static prototypes, allowing for stronger collaboration in development teams. It has great support for Spring:

http://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html

Lets explore Thymeleaf's in the working Spring Boot sample.

There is support for templates to centralize common code. We can also see how it can be used to display informtation passed from a backing controller.

Question: Why not display HTML or Javascript on it own? PCF has a static build pack. Why put this in Spring Boot?

## Add The Controllers

```java

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class ProductController {
	private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);
	private final ProductService productService;

	@GetMapping("/products")
	ResponseEntity<List<Product>> products() {
		List<Product> returnValue = this.productService.getAllProducts();
		if (returnValue != null) {
			LOG.info("All products returned");
			return ResponseEntity.ok(returnValue);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping("/products")
	ResponseEntity<Product> createProducts(@RequestBody @Valid Product product) {
		try {
			return ResponseEntity.ok(this.productService.createUpdateProduct(product));
		} catch(RestClientException | DataIntegrityViolationException | ConstraintViolationException e) {
			LOG.error("error thrown during create/update of product", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/products/{productId}")
	ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
		if(this.productService.deleteProduct(productId))
			return ResponseEntity.ok("Product deleted successfully");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product does not exist");
	}

}

```
Important concepts are:

- @RestController
- @RequestMapping
- @GetMapping, @PutMapping, @DeleteMapping
- ResponseEntity

Question: Why use an args constructor? Why not autowire what we need into the Controller?

Before going on to the services lets take a look at the Product Service.

## Switch To The Product Service

## Review The Services

Add The Services To call the product service, cover RestTemplate

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



