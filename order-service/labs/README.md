# Lab 2

These are hands on exercises to demonstrate working with some of the key concepts in the Order Service

# Order Service

## Purpose
This service will have two rest-endpoints
1.  Create an order
2.  Get all orders

## Pre-Requisites for running in local
1. Java 8
2. Docker should be installed in your system
  * Windows 7,  [use Docker Toolbox](https://www.docker.com/products/docker-toolbox). When installing, make sure to select **ALL** the install options
  * Mac,  [use docker for Mac](https://docs.docker.com/docker-for-mac/)
  * Once the docker is running, you will need to run redis cache in docker

3. Once Docker is installed we need to run [Redis](https://redis.io/documentation) in docker
add docker compose file at order-service/docker-compose.yml
```java
version: "3"
services:
 redis:
  image: redis:alpine
  container_name: training-redis
  ports:
    - "6379:6379"
```
  * On mac, open your terminal and run the below command
  * On windows, start Docker (Docker Quick Start Terminal), go to the training module path. Run the below command

```java
$ docker-compose -d up
```
4. Lombok Setup for IntelliJ
This set up is required to enable Lombak annotations to work in your local IDE:
  * Install Lombak plugin
  * Enable annotation processing (Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors -> Enable annotation processing)

5. Good to have - PostMan, from [Google webstore](https://www.google.ca/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0ahUKEwi2te35xJLTAhXn3YMKHXJmAogQFggjMAA&url=https%3A%2F%2Fchrome.google.com%2Fwebstore%2Fdetail%2Fpostman%2Ffhbjgbiflinjbdggehcddcbncdddomop%3Fhl%3Den&usg=AFQjCNE_Yq59TT1ZExzJ68FTldg4ho_lGw&sig2=oDm4-jzg6EBrl9oqurNFIQ)


## Generating the spring boot application
Go to  [Spring Initializr](http://start.spring.io/)
Please read the following instructions before generating the project.
On the screenshot below there are couple of important points to note
* Select Gradle project
* Do not change the spring boot version (It always defaults to the latest 1.5.2 )
* Search for dependencies, see examples of dependencies on the right side of the screenshot

Add the dependencies needed for the project like
* AMQP - [read more on AMQP](https://projects.spring.io/spring-amqp/)
* Stream Rabbit [read more on messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)
* Cloud Connectors [read more on cloud connectors](http://cloud.spring.io/spring-cloud-connectors/)
* Lombok [read more on Lombok](https://projectlombok.org/)


![alt text](Spring_Initializr.png)

## Dependencies in build.gradle

Once the project has been generated, it will be downloaded to your system as a zip file. Extract it to any prefered location. Open the project in your favorite IDE.
You will see that the basic project structure will be made for you.
Take a look at the **build.gradle**. In the code snippet below you can see all the dependencies has been added.

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
To start the application, right click the application select 'Run OrderServiceApplication main()'.
You can see the server startup in the console.

## Separation of profiles for our application
Let's add a dev profile for our project
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
A YAML file is actually a sequence of documents separated by --- lines, and each document is parsed separately to a flattened map.

If a YAML document contains a spring.profiles key, then the profiles value (comma-separated list of profiles) is fed into the Spring Environment.acceptsProfiles() and if any of those profiles are active, then that document is included in the final merge (otherwise not).

Once this is done, add the below line to your run configuration or to your application.properties\ run configurations

```java
spring.profiles.active:dev
```
Let's restart the server. You should see the following line in the console
```java
.training.OrderServiceApplication     : The following profiles are active: dev
```

## Creating The Model

Create a 'model' package to add to your pojo classes.
Let's make the model class have the following variables :
* orderID
* userName
* fulfilled - boolean value

at /src/main/java/com/cloudnativecoffee/model/Order.java
```java
//lombok annotations
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order implements Serializable{
    private static final long serialVersionUID = 3734899149255587948L;

    private String orderId;
    private String userName;
    private Boolean fulfilled;
}
```
Note the lombok annotations! you can check out the various functionalities of lombok [here](https://projectlombok.org/).

## Create a Repository for CRUD operations

Let's create a repository to perform CRUD operation on the order object. For this we will create a package called **repository** in our project struture. Add an interface **OrderRepository**.
This class should be annotated with @Repository annotation.

What does this annotation do?
Indicates that the beans will be eligible for persistence exception translation.

The interface should extend CrudRepository, as it will be leveraging CRUD functionalities. For this we will have to add JPA dependency to our build.gradle

```java
compile("org.springframework.boot:spring-boot-starter-data-jpa")
```
at /src/main/java/com/cloudnativecoffee/order/repository/OrderRepository.java
```java
import com.hopper.training.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, String>{
	List<Order> findByUserName(String userName);
}

```
**OrderRepository** extends the CrudRepository interface. The type of entity and ID that it works with,Order and String, are specified in the generic parameters on CrudRepository. By extending CrudRepository, OrderRepository inherits several methods for working with Order persistence, including methods for saving, deleting, and finding Order entities.

Spring Data JPA also allows you to define other query methods by simply declaring their method signature. In the case of OrderRepository, this is shown with a findByUserName() method.

In a typical Java application, you’d expect to write a class that implements OrderRepository. But that’s what makes Spring Data JPA so powerful: You don’t have to write an implementation of the repository interface. Spring Data JPA creates an implementation on the fly when you run the application.
More details of spring JPA repositories can be found here - [Getting Started with Spring JPA](https://spring.io/guides/gs/accessing-data-jpa/)

## Adding Redis cache to our application
Inorder to add the redis cache functionality, let's change our POJO order class. All we need to do is add the RedisHash annotation to  /src/main/java/com/cloudnativecoffee/model/Order.java

```java
@RedisHash("Order")
//lombok annotations
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

    private Boolean fulfilled;
}
```
RedisHash marks Objects as aggregate roots to be stored in a Redis hash.

Properties annotated with @Id as well as those named id are considered as the identifier properties. Those with the annotation are favored over others. More on this at [Spring Data Redis](http://docs.spring.io/spring-data/redis/docs/current/reference/html/)

Note that the property named OrderId annotated with org.springframework.data.annotation.Id and a @RedisHash annotation on its type. Those two are responsible for creating the actual key used to persist the hash. Taken from [Spring Data Redis](http://docs.spring.io/spring-data/redis/docs/current/reference/html/)

## Create exception class
* create exception package at /src/main/java/com/cloudnativecoffee/order/exception
* create java class OrderCreationException.java.
* This exception class will be called when orderCreate method throws DataIntegrityViolationException

```java
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OrderCreationException extends RuntimeException {
    private static final long serialVersionUID = 923761071817133132L;

    public OrderCreationException(String message) {
        super(message);
    }

    public OrderCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

## Creating a service class
1. Create a **service** package in your project structure /src/main/java/com/cloudnativecoffee/order/service/OrderService.java
2. Create OrderService in the package
3. Annotate OrderService with @Service
  * Indicates that an annotated class is a "Service"
```java
import static com.google.common.base.Preconditions.checkNotNull;
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepo;

    public OrderServiceImpl(OrderRepo orderRepo) {
        this.orderRepo = checkNotNull(orderRepo);
    }

    @Override
    public List<Order> listAllOrders() {
        return Lists.newArrayList(orderRepo.findAll());
    }

    @Override
    public List<Order> listAllOrdersForUser(String userName) {
        return orderRepo.findByUserName(userName);
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        try {
            order.setOrderId(UUID.randomUUID().toString());
            order.setFulfilled(true);
            return orderRepo.save(order);
        } catch (DataIntegrityViolationException e) {
            log.error("redis save threw an error", e);
            throw new OrderCreationException("Failed to create an order", e);
        }
    }
```

## Creating a Simple RestEndpoint
1. Create a **controller** package in your project structure at /src/main/java/com/cloudnativecoffee/order/controller
2. Create OrderController.java in controller package
3. Annotate OrderController with @RestController
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
        return this.orderService.getAllOrders();
    }

    @GetMapping(value = "/order/{userName}" ,
            produces = "application/json")
    public ResponseEntity<List<Order>> listAllOrdersForUser(@PathVariable String userName) {
        return this.orderService.getAllOrderForUser(userName);
    }

    @PostMapping(value = "/order",
            consumes =  "application/json",
            produces = "application/json")
    public ResponseEntity<Order> createOrder(@RequestBody @Valid Order order) {
        log.info("Order placed : "+ order.toString());
        return ResponseEntity.ok(this.orderService.createOrder(order));
    }
```

Let's test out the application now
