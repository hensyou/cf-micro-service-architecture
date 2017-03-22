# Order Service

## Purpose
This service does CRUD on orders. It can also place an order.

## Creating The Service

The service can be created the same as the market place. The following dependancies are required:

```java

dependencies {
	compile('org.springframework.boot:spring-boot-starter-data-redis')
	compile ('org.springframework.boot:spring-boot-starter-web')
    compile ('com.google.guava:guava:20.0')
	compile('org.springframework.boot:spring-boot-starter-amqp')
	compile ('org.springframework.cloud:spring-cloud-starter-stream-rabbit:1.1.3.RELEASE')
	compile ('io.github.benas:random-beans:3.0.0')
    compileOnly('org.projectlombok:lombok:1.16.14')

	testCompile('org.springframework.boot:spring-boot-starter-test')
    testCompile("junit:junit")
}

```
These are similar to the Product Service with one acceptions.

## Creating The Model

Our Order pojo looks like this:

```java

@RedisHash("Order")
@Data
@NoArgsConstructor
public class Order implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @Setter @Getter
    private String orderID;

    @Indexed
    @Setter @Getter
    private String userName;

    @Setter @Getter
    private List<Product> productList = new ArrayList<Product>();

    @Setter @Getter
    private Boolean fulfilled;

}

```

Important Concepts:
1. @RedisHash("Order")
2. @Indexed

As you can guess this service will be backed with Redis.






