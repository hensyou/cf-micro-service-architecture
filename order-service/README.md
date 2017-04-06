# Order Service

## Purpose
This service will have two rest-endpoint
1.  Create an order.
2.  Get all orders

## Pre-Requisites for running in local
1. Java 8 installed
2. Docker should be installed in your system
  * Windows 7,  [use Docker Toolbox](https://www.docker.com/products/docker-toolbox). Check **ALL**  the install options
  * Mac,  [use docker for Mac](https://docs.docker.com/docker-for-mac/)


## Creating the spring boot application
Go to  [Spring Initializr](http://start.spring.io/)
Add the dependencies needed for the project like
* AMQP
* Stream Rabbit
* Cloud Connectors
* Lombok
![alt text](Spring_Initializr.png)

## Dependencies in build.gradle

The service can be created the same as the market place. The following dependancies are required:

```java

dependencies {
	compile('org.springframework.boot:spring-boot-starter-amqp')
	compile('org.springframework.boot:spring-boot-starter-cloud-connectors')
	compile('org.springframework.cloud:spring-cloud-starter-stream-rabbit')
	compileOnly('org.projectlombok:lombok')
	testCompile('org.springframework.boot:spring-boot-starter-test')
}

```
## Let's start the application
Start the application. Right click the application, select 'Run OrderServiceApplication main()'.
You can see the server startup in the console.

## Separation of profiles for our application
Create 'application.yml' under src/main/resources.
```yml
spring:
  application:
    name: order-service
---
spring:
  profiles: dev
server:
    port: 8084
```

Once this is done, add this pertiualr line to your run configuration or to your application.properties
```java
spring.profiles.active:dev
```
Let's restart the server. You should be see the below line in the console
```java
.training.OrderServiceApplication     : The following profiles are active: dev
```

## Creating The Model

Create a 'model' package to add in your pojo classes.
Let's make the model class have the following variables :
* orderID
* productName
* quantity

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private Integer order_id;
    private String productName;
    private Integer quantity;
}
```
Note the lombok annotations! you can check out the various functionalities of lombok [here](https://projectlombok.org/).

## Adding The Repository

We will be adding repository to perform CRUD operation on the order object. For this we will create a package called **repository** in our project struture. Add an interface **OrderRepository**
Repository classes will be annotated with @Repository annotation.

What does this annotation do?
Indicates that the annotated class is a 'Repository', Beans are eligible for persistence exception translation.

The interface should extend CrudRepository, as it will be leveraging CRUD functionalities.
also, you need to add the following dependency to your build.gradle
```java
compile("org.springframework.boot:spring-boot-starter-data-jpa")
```
TODO: explain more about what this dependency does.
Our repository class should look like this :

```java
import com.hopper.training.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {
}
```
## Adding redis cache to our application
Inorder to add the redis cache functionality, let's change our POJO order class. All we need to do is add the RedisHash annotation into it,


## Creating a service class
1. Create a **service** package in your project structure
2. create OrderService in the package
3. annotate OrderService with @Service
  * Indicates that an annotated class is a "Service"
```java
import static com.google.common.base.Preconditions.checkNotNull;
@Service
//lombok
@Slf4j
public class OrderService {
    private final OrderRepo orderRepo;

    public OrderService (OrderRepo orderRepo) {
        this.orderRepo = checkNotNull(orderRepo);
    }

    public List<Order> listAllOrders() {
        return Lists.newArrayList(orderRepo.findAll());
    }
```

## Creating a simple RestEnpoint
1. Create a **controller** package in your project structure
2. create OrderController in controller package
3. annotate OrderController with @RestController
  * The classes carrying this annotation are treated as controllers.

Take a look at the Controller below:

```java
@RestController
@RequestMapping("/v1")
@Slf4j
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = checkNotNull(orderService);
    }

    @GetMapping(value = "/order" ,
            produces = "application/json")
    public ResponseEntity<List<Order>> listAllOrders() {
        return ResponseEntity.ok(this.orderService.listAllOrders());
    }
}
```

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