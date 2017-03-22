# Product Service

## Purpose
This service does CRUD on the product data base exposing the products via REST

## Important Concepts
1. Data Patterns - Repositories
2. Testing
3. Profiles - development vs qa vs cloud
4. Flyway
5. Rest Controllers

## Creating The Service

We can follow the same approach as the Market Place service to create the Base Gradle Project.

The following dependancies are added to give us the functionality to support this service.

```java

dependencies {
	compile('org.springframework.boot:spring-boot-starter-data-jpa')
	compile('org.springframework.boot:spring-boot-starter-data-rest')
	compile('org.flywaydb:flyway-core:4.1.2')
	compile('org.postgresql:postgresql')
	compileOnly('org.projectlombok:lombok:1.16.14')
	compile ('com.google.guava:guava:20.0')
	runtime('com.h2database:h2')
	compile ('org.springframework.cloud:spring-cloud-starter-stream-rabbit:1.1.3.RELEASE')
	compile ('io.github.benas:random-beans:3.0.0')
	compile "org.springframework.security.oauth:spring-security-oauth2"
	compile group: 'org.springframework.security', name: 'spring-security-jwt', version: '1.0.7.RELEASE'
	testCompile('org.springframework.boot:spring-boot-starter-test')
	testCompile "junit:junit:4.12"
}

```

Lets discuss each of them.

## Profiles

We use profiles to have our code behave differently depending on where we run it. Lets look at how profiles are used in the Product Service.

```java

server:
  port: 8081
spring:
  h2:
    console:
      enabled: true
  datasource:
    url: jdbc:h2:mem:product
  rabbitmq:
    host: localhost
    port: 5672
flyway:
  clean-on-validation-error: true

security:
  ignored: /favicon.ico
  basic:
    enabled: false
---
spring:
  profiles: dev-postgres
  datasource:
    url: jdbc:postgresql://127.0.0.1:5432/product_service_dataservice
    username: postgres
    password: root
    driver-class-name: org.postgresql.Driver

server:
  port: 8082
---
spring:
  profiles: cloud

```
Lets see how we can change profiles? Where does the profile 'cloud' get invoked. How does VCAPS services work compared to these?

## Database Options

We have H2 configured for local development and testing:

http://localhost:8081/h2-console/

The database is created using Flyway. Lets review Flyway.

## Flyway

We have added Flyway to help us migrate the DB schema as our application envolves. In resource/db/migrations we can put the SQL for each migration. The naming pattern is V#__(name of the migration).

Lets explore how this is configured and how it works with migrations.

## Creating A Pojo

We need to create the Product object, this one is different than the Market Place as it needs to persist to a DB:

```java

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

```
Lets discuss what is here. Lets review some basics on JPA in Spring Boot.


## Repository

Next we need a way to perform the CRUD options.

```java

@Repository
@ComponentScan("com.cloudnativecoffee.model")
public interface ProductRepo extends PagingAndSortingRepository<Product, Long> {
    Product findByName(String Name);
}

```
It does not look like much, but Spring Data Repositories are a very powerful tool for us. Lets discuss the benifits and look at how they can be extended.

## Making The Service A Resource Service

To ensure data is only returned when a valid token is supplied, the following configuration is added:

```java

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	private static final Logger logger = Logger.getLogger(ResourceServerConfig.class);
	
	@Value("${security.oauth2.client.clientId:default_resource_id}")
	private String resourceId;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenServices(tokenServices());
		logger.debug("INJECTED_RESOURCE_ID: "+ resourceId);
		resources.resourceId(resourceId);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.addFilterAfter(new BearerTokenOncePerRequestFilter(), AbstractPreAuthenticatedProcessingFilter.class)
			.antMatcher("/**")
		      .authorizeRequests()
		        .antMatchers("/h2-console/**")
		        .permitAll()
		      .anyRequest()
		        .authenticated();
	}
	
 
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }
 
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
        return converter;
    }
 
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }



}

```

Let discuss this from a high level, in your enterprise the details on the token creation will differ.

## Messaging Components

This service recieves a call from the Order service to place an order. This checks if there is enough product and modifies the accordingly.

Messaging is driven by RabbitMQ. Our channels (Topics in Rabbit MQ) are configured in this way:

```java

public interface ProductChannels {

    @Input
    SubscribableChannel orderChannel();
    
    @Output
    MessageChannel confirmationChannel();

}

```


Here is our listener, when this receives a message on the Order channel it performs the business logic and then puts a confirmation back on the confirmationChannel (which goes back to the Order service so it can persist an Order)

```java

MessageEndpoint
public class ProductMessageListener {
    private final ProductRepo productRepo;

    public ProductMessageListener(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @StreamListener(value = "orderChannel")
	@SendTo("confirmationChannel")
    @Transactional
    public Order process(Order order) {
		List<Product> completedOrder = new ArrayList<Product>();
		int itemsFulfilled = 0;
		int itemsToFulfill = order.getProductList().size();
		for (Product orderedProduct : order.getProductList()) {
			//get orderedProduct from repo
			Product totalProduct = productRepo.findOne(orderedProduct.getId());
			if (totalProduct.getQuantity() >= orderedProduct.getQuantity()) {
				totalProduct.setQuantity(totalProduct.getQuantity()-orderedProduct.getQuantity());
				completedOrder.add(totalProduct);
				itemsFulfilled++;
			}
		}
		if (itemsFulfilled == itemsToFulfill) {
			productRepo.save(completedOrder);
			order.setFulfilled(true);
		}
		else {
			order.setFulfilled(false);
		}
		return order;
    }

}

```

Here is how the Order confirmation is written (the sendTo in the listener calls this):

```java

@MessagingGateway
public interface ProductMessageWriter {

    @Gateway(requestChannel = "confirmationChannel")
    void write(Order order);
    
}

```

## Test

Lets review how messaging is tested:

```java

public class ProductMessageListenerTest {

    private ProductMessageListener productMessageListener;
    private final String orderId = UUID.randomUUID().toString();
    @Mock
    private ProductRepo productRepo;
    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        this.productMessageListener = new ProductMessageListener(productRepo);
    }

    @Test
    public void checkProductRepoCalled() {
        when(productRepo.findOne(any())).thenReturn(dummyProductObjectCreate(10));
        Order order = productMessageListener.process(dummyOrderObjectCreate(1));
        verify(this.productRepo, times(1)).findOne(any());
        assertEquals(true, order.getFulfilled());
    }

    @Test
    public void checkFulfilledFail() {
        when(productRepo.findOne(any())).thenReturn(dummyProductObjectCreate(2));
        Order order = productMessageListener.process(dummyOrderObjectCreate(3));
        verify(this.productRepo, times(1)).findOne(any());
        assertEquals(false, order.getFulfilled());
    }

    private Product dummyProductObjectCreate(int quantity) {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        Product product = enhancedRandom.nextObject(Product.class);
        product.setQuantity(quantity);
        return product;
    }

    private Order dummyOrderObjectCreate(int quantity) {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        Order order = enhancedRandom.nextObject(Order.class);
        order.setProductList(Arrays.asList(dummyProductObjectCreate(quantity)));
        return order;
    }


```
Here is how the Service layer is tested:

```java

public class ProductServiceTest {
    private ProductService productService;
    @Mock
    ProductRepo productRepo;
    @Captor
    private ArgumentCaptor<Product> productCaptor;
    @Captor
    private ArgumentCaptor<Long> productIdCapture;

    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        this.productService = new ProductServiceImpl(productRepo);
    }

    @Test
    public void verifyFindAllInvoked() {
        when(productRepo.findAll()).thenReturn(new ArrayList<Product>());
        productService.getAllProducts();
        verify(this.productRepo, times(1)).findAll();
    }

    @Test
    public void verifyRepoSaveInvoked() {
        productService.createUpdateProduct(dummyProductObjectCreate(10));
        verify(this.productRepo, times(1)).save(productCaptor.capture());
        Product product = productCaptor.getValue();
        assertEquals("dummy-product-name", product.getName());
    }

    @Test
    public void verifyRepoDelete() {
        productService.deleteProduct(new Long(123));
        verify(this.productRepo, times(1)).delete(productIdCapture.capture());
        assertEquals(new Long(123), productIdCapture.getValue());
    }

    private Product dummyProductObjectCreate(int quantity) {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        Product product = enhancedRandom.nextObject(Product.class);
        product.setQuantity(quantity);
        product.setName("dummy-product-name");
        return product;
    }

}

```

