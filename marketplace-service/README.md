# Market Place Service

## Purpose

This service displays a unified view of the backing services for the client to interact with.

## Important Concept

1. Creating a UI Application with Spring Boot
2. Rest Template To Calling A Backing Servers
3. Circuit Breaker To Protect When Backing Services Are Down
4. Profiles For Different Behavior In Different Environments
5. Gradle
6. Spring Boot Concepts (Spring Boot Starters, Auto Configuration, Controllers, Services)

## Create The Service

Create the service:

```shell

curl https://start.spring.io/starter.tgz -d style=web,thymeleaf -d groupId=com.cloudnativecoffee -d name=marketplace-service -d type=gradle-project | tar -xzvf -

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

Let's review other UI options that can be ran within Spring Boot.

## Review The Services

Its good to put logic in a service and leave that Controller to route requests and responses.

Here is a sample of a service:

```java

@Service
public class ProductService {
	private final Logger LOG = Logger.getLogger(ProductService.class);
	private static final String FALLBACK_METHOD = "genericErrorMessage";
	private final String productServiceHost;
	private final String productBaseurl;
	private OAuth2RestTemplate restTemplate;

	public ProductService(@Value("${marketplace.services.product.id}") String productServiceHost,
						  @Value("${marketplace.services.product.api.url}") String productBaseurl,
						  OAuth2RestTemplate restTemplate) {
		this.productServiceHost = productServiceHost;
		this.productBaseurl = productBaseurl;
		this.restTemplate = restTemplate;
	}

	@HystrixCommand(
			fallbackMethod = FALLBACK_METHOD,
			commandProperties={
				@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="2000"),
				@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
				
			}
	)
	public ResponseEntity<List<Product>> getAllProducts() {
		ParameterizedTypeReference<List<Product>> parameterizedTypeReference = new ParameterizedTypeReference<List<Product>>() {};
		String apiUrl = new StringBuilder().append(productServiceHost).append(productBaseurl).toString();
		
		OAuth2AccessToken token = restTemplate.getAccessToken();
		LOG.debug("token: " + token);
		
		List<Product> products = restTemplate.exchange(apiUrl, HttpMethod.GET, null, parameterizedTypeReference).getBody();
		return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
	}

	public ResponseEntity<List<Product>> genericErrorMessage(Throwable exception) {
		LOG.error("An error has occurred. Callback method called", exception);
		return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
	}
}

```
Key concepts:
- @Service
- @HystrixCommmand
- ParameterizedTypeReference
- HttpStatus

## Added Security

Before reviewing the security, lets review Spring Security, OAuth and JWT.

To secure the market place application we can add the following:

```java

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
	        .authorizeRequests()
	        .anyRequest().authenticated()
	        .and()
	        .formLogin().permitAll()
	        .defaultSuccessUrl("/");
    }
}

```
This ensures all requests are authenticated and a login form is displayed (Spring Security will generate it).

Our user name and password are configured in the application.yml

```java

security:
  ignored: /favicon.ico
  basic:
    enabled: false
  user:
    name: admin
    password: admin

```
Question: Obviously this is not production ready. What are some things we can do?

Next we need to be redirected to the Authorization Server to get a Token that can be used to talk to Resource Services:

```java

oauth2:
    client:
      client-id: adminclient
      client-secret: adminsecret
      scope: read,write
      auto-approve-scopes: '.*'
      access-token-uri: http://localhost:9999/oauth/token
      user-authorization-uri: http://localhost:9999/oauth/authorize
      grant_type: authorization_code
    resource:
      user-info-uri: http://localhost:9999/me
      
```
We need this to make the Rest calls from the Service and get resources.

Here is how a OAuth aware Rest Template is configured:

```java

@Configuration
@EnableOAuth2Client
public class ServicesConfig {

	@Bean
	@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
	OAuth2RestTemplate restTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
		return new OAuth2RestTemplate(resource, context);
	}

}

```

This will ensure the token value is in the session, which the Resource servers will expect before returning data.

These are the important concepts in this service. Lets take some time to discuss, or work on building some of these ideas.




