# Order Service

## Purpose
This service does CRUD on orders. It can also place an order.


## Creating the spring boot application
Go to  [Spring Initializr](http://start.spring.io/)
Add the dependencies needed for the project like
* Redis
* AMQP
* Stream Rabbit
* Cloud Connectors
* Lombok
![alt text](Spring_Initializr.png)

## Dependencies in build.gradle

The service can be created the same as the market place. The following dependancies are required:

```java

dependencies {
	compile('org.springframework.boot:spring-boot-starter-data-redis')
	compile ('org.springframework.boot:spring-boot-starter-web')
    compile ('com.google.guava:guava:20.0')
	compile('org.springframework.boot:spring-boot-starter-amqp')
	compile ('org.springframework.cloud:spring-cloud-starter-stream-rabbit:1.1.3.RELEASE')
	compile 'org.springframework.cloud:spring-cloud-spring-service-connector:1.2.3.RELEASE'
	compile 'org.springframework.cloud:spring-cloud-cloudfoundry-connector:1.2.3.RELEASE'
	compile ('io.github.benas:random-beans:3.0.0')
    compileOnly('org.projectlombok:lombok:1.16.14')
	compile("io.springfox:springfox-swagger2:2.6.1")
	compile("io.springfox:springfox-swagger-ui:2.6.1")

	testCompile('org.springframework.boot:spring-boot-starter-test')
    testCompile("junit:junit")
}

```

## Creating The Model

Create a 'model' package to add in your pojo classes.
Let's make the model class have the following variables :
* orderID
* userName
* productList
* fulfilled - boolean flag to check if fulfilled is accepted

Also, we want to use **Redis Cache** for storing the orders created. Let's make these orders to expire in 10min.
Our Order would look like this :

```java
@RedisHash(timeToLive=600)
//lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order implements Serializable{

    private static final long serialVersionUID = 3734899149255587948L;

    @Id
    private String orderId;

    @Indexed
    @NotEmpty
    private String userName;

    @NotEmpty
    private List<Product> productList = new ArrayList<>();

    private Boolean fulfilled;

}
```K

Important Concepts to be noted from the POJO:
1. @RedisHash
  * RedisHash marks Objects as aggregate roots to be stored in a Redis hash.
  * timeToLive - Time before expire in seconds.
2. @Id
  * Demarcates the identifier
3. @Indexed
  * the property marked with this annotation will be included in a secondary index. The value will be part of the key built for the index

## Adding The Repository

We will be adding repository to perform CRUD operation on the order object. For this we will create a package called **repository** in our project struture. Add a class **OrderRepo**
Repository classes will be annotated with @Repository annotation.

What does this annotation do?
Indicates that the annotated class is a 'Repository', Beans are eligible for persistence exception translation.

```java

@Repository
public interface OrderRepo extends CrudRepository<Order, String>{

	List<Order> findByUserName(String userName);
}

```
Lets discuss this including what the access method that is added will do. How does this compare to the Product Repo?
Also, take a look at the CrudRepository

## Messaging

Messaging is similar to the Product service. The main difference is the Order service is listening for a confirmation of an Order before doing its update to the backing service:

```java

@MessageEndpoint
public class OrderMessageListener {
	private static final Logger LOG = LoggerFactory.getLogger(OrderMessageListener.class);
	private final OrderRepo orderRepo;

	public OrderMessageListener(OrderRepo orderRepo) {
		this.orderRepo = orderRepo;
	}

    @StreamListener(value = "confirmationChannel")
    public void checkOrderConfirmation(Order order) {
		LOG.info("order confirmation saved, status is "+order.getFulfilled());
    	orderRepo.save(order);
    }

}

```