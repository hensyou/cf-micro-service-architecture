# Labs

The following are some of hands on exercises to reinforce key concepts in the Marketplace Service

## Concepts To Learn
1. Create a simple UI using Spring Boot and ThymeLeaf
2. Create a simple Product Backing Service using Spring Boot
3. Communicate Via Rest Between Two Services
4. Wrapping a service call with circuit breaker
5. Deploying To PCF
6. Working with Basic Security

## Building Out The Rest Services

### Create The Project

Lets create a basic Rest Client (product-service), this can be done from the command line if you have curl.

```shell

curl https://start.spring.io/starter.tgz -d style=web,actuator -d groupId=com.sample -d name=product-service -d type=gradle-project | tar -xzvf -

```
It can also be done using start.spring.io, you can select the projects on the form (this is what the curl command posts too).

![Architecture](images/project_create.png)

Lastly you can create it with a wizard in STS or IntelliJ.

![Architecture](images/sts_project_create.png)

Once created, import the project into your workspace (using Import Gradle/Maven). If you made this project using an IDE plugin, it will already be created.


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

https://projectlombok.org/download.html

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

Add the following to your application.properties.

```shell

endpoints.sensitive=false

```

Restart your application and try some of the end points:

- http://localhost:8080/mappings
- http://localhost:8080/health
- http://localhost:8080/env

### Deploy to Cloud Foundry

Create a file in the root of the project called 'manifest.yml'. Add the following to this file:

```shell

---
applications:
- name: product-service
  memory: 1024M
  buildpack: java_buildpack
  path: target/product-service-0.0.1-SNAPSHOT.jar
  routes:
  - route: product-service.cfapps.io


```

Ensure to clean and build your Gradle project and then deploy to PWS by running `cf push` from the same folder as the manifest.yml. Also you can deploy to PCF using the STS plugin. If the host is already taken, update the `route` name to be more unique in the manifest.yml file.


## Building Out The UI Client

### Creating the Project

Lets build out a basic UI.  We will call this ui-service.

```shell

curl https://start.spring.io/starter.tgz -d style=web,thymeleaf,actuator -d groupId=com.sample -d name=ui-service -d type=gradle-project | tar -xzvf -

```

If you are using start.spring.io, simply select the following projects.

![Architecture](images/project_create.png)

Once created, import into your IDE of choice (ie: import existing Gradle project).

### Adding A Thymeleaf Template

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

### Adding A Simple Controller

Create a simple class to display the index page and put some data into it:

```java

@Controller
public class HomeController {
	

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("name", "Tug Speedman!");
		return "index";
	}

}

```

### Deploy To PCF

Add a manifest to the root of the project, and put the following:

```shell

---
applications:
- name: simple-ui
  memory: 1024M
  buildpack: java_buildpack
  path: target/simple-ui-0.0.1-SNAPSHOT.jar
  routes:
  - route: simple-ui.cfapps.io

```
Do a clean/build and then `cf push` the application to your PCF space.

### Using RestTemplate To Communicate Between Services

Lets connect the services to together adding RestTemplate.

#### Wire In A RestTemplate

Update your main class to have the following bean definition.

```java

@SpringBootApplication
public class UiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UiServiceApplication.class, args);
	}
	
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}


```

#### Creating the Service Class

Create a Service Class in the simple-ui list the following

```java

@Service
public class ProductService {

	private RestTemplate restTemplate;

	public ProductService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public List<Product> getProducts() {
		ParameterizedTypeReference<List<Product>> parameterizedTypeReference = new ParameterizedTypeReference<List<Product>>() {
		};
		List<Product> products = restTemplate
				.exchange("http://localhost:8080/v1/product", HttpMethod.GET, null, parameterizedTypeReference)
				.getBody();
		return products;
	}
}

```
Wire this into the main controller.

```java

@Controller
public class HomeController {
	
	private ProductService productService;
	
	public HomeController(ProductService productService) {
		this.productService = productService;
	}




	@GetMapping("/")
	public String home(Model model) {
		List<Product> products = productService.getProducts();
		model.addAttribute("products", products);
		return "index";
	}

}

```

Update the UI to reflect the new change.

```html

<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Simple UI</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
	
	<p>Here is what we have today!</p>
    
    <table>
    <tr th:each="product : ${products}">
        <td th:text="${product.name}">1</td>
    </tr>
    </table>
    
</body>
</html>

```

## Deploy To PCF

Rebuild the UI service and push it to PCF again.

## Adding A Circuit To the Call

In the event the product service goes dowm, we want an intelligent fallback. Hystrix will give us this.

Add the following to your build.gradle

```shell

compile group: 'org.springframework.cloud', name: 'spring-cloud-starter-hystrix', version: '1.2.6.RELEASE'

```

Enable Circuit Breaker by adding the following annotation to your main Spring Boot class.

```java

@SpringBootApplication
@EnableCircuitBreaker
public class UiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UiServiceApplication.class, args);
	}
	
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}

```

Now add the Hystrix configurations to your service class.

```java

@Service
public class ProductService {

	private RestTemplate restTemplate;

	public ProductService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@HystrixCommand(fallbackMethod = "fallBack", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000") })
	public List<Product> getProducts() {
		ParameterizedTypeReference<List<Product>> parameterizedTypeReference = new ParameterizedTypeReference<List<Product>>() {
		};
		List<Product> products = restTemplate
				.exchange("http://localhost:8080/v1/product", HttpMethod.GET, null, parameterizedTypeReference)
				.getBody();
		return products;
	}

	public List<Product> fallBack() {
		List<Product> product = new ArrayList<Product>();
		Product product1 = new Product();
		product1.setName("Rocket Ship");
		product.add(product1);
		return product;
	}

}


```

Rebuild the project and deploy to PCF. To test this, kill the product-service. You should get a rocket ship.

## Adding Security To The Product Service

Update the gradle of the product service to have the following'

```shell

compile group: 'org.springframework.boot', name: 'spring-boot-starter-security', version: '1.5.2.RELEASE'

```

Next add a security configuration class

```java

	@Configuration
	@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${username}")
	private String admin_username;

	@Value("${password}")
	private String admin_password;

	@Value("${role}")
	private String admin_role;
	
	 @Override
	  protected void configure(HttpSecurity http) throws Exception {
	    http.authorizeRequests().anyRequest().fullyAuthenticated();
	    http.httpBasic();
	    http.csrf().disable();
	  }


	/**
	 * We are using the in-memory for now
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(admin_username).password(admin_password).roles(admin_role);
	}
}

```

Update the application.properties/application.yml to contain the following:

```shell

username: admin
password: admin
role: USER

```

Rebuild and redeploy. Now we cannot hit the end point without supplying credentials (Postman can be used to simulate this). Note, when testing in the browser, ensure to use In Cognito windows

Rebuild and redeploy this application.

## Update the UI Application

Added the credentials to the application.properties/application.yml

```shell

endpoints.sensitive=false
server.port=8090
username: admin
password: admin

```
A better approach to sharing configurations like this is to use Config Server:

https://docs.pivotal.io/spring-cloud-services/1-3/common/config-server/

Update the Service to add an authentication header:

```java

@Service
public class ProductService {

	private RestTemplate restTemplate;
	
	@Value("${username}")
	private String admin_username;

	@Value("${password}")
	private String admin_password;

	public ProductService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	  private HttpHeaders getHeaders(){
	        String plainCredentials= admin_username + ":" + admin_password;
	        String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
	         
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Authorization", "Basic " + base64Credentials);
	        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	        return headers;
	    }

	@HystrixCommand(fallbackMethod = "fallBack", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000") })
	public List<Product> getProducts() {
		ParameterizedTypeReference<List<Product>> parameterizedTypeReference = new ParameterizedTypeReference<List<Product>>() {
		};
		 HttpEntity<String> request = new HttpEntity<String>(getHeaders());
		List<Product> products = restTemplate
				.exchange("http://localhost:8080/v1/product", HttpMethod.GET, request, parameterizedTypeReference)
				.getBody();
		return products;
	}

	public List<Product> fallBack() {
		List<Product> product = new ArrayList<Product>();
		Product product1 = new Product();
		product1.setName("Rocket Ship");
		product.add(product1);
		return product;
	}

}

```
Recompile and deploy to PWS to test
