# Auth Server
This is to simulate an OAuth Server, it issue JWT tokens that can used to secure a service.

## Create the service

```bash

curl https://start.spring.io/starter.tgz -d style=web -d groupId=com.cloudnativecoffee -d name=auth-server | tar -xzvf -

```

Update the dependancies:

```shell

dependencies {
	compile('org.springframework.boot:spring-boot-starter-web')
	testCompile('org.springframework.boot:spring-boot-starter-test')
	compile "org.springframework.boot:spring-boot-starter-security"
	compile "org.springframework.security.oauth:spring-security-oauth2"
}


```

Add the @EnableAuthorizationServer annotation

```java

@SpringBootApplication
@EnableAuthorizationServer
public class AuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}
}

```

Save the application.properties as application.yml and add the following config:

```shell

security:
  user:
    name: admin
    password: admin
    role: USER
  oauth2:
    client:
      client-id: adminclient
      client-secret: adminsecret
      scope: read,write
      auto-approve-scopes: '.*'

```

Test for a token using the Admin Id and Secret (local):

```bash

curl adminclient:adminsecret@localhost:8080/oauth/token -d grant_type=client_credentials

```
Running in PCF:

```bash

curl adminclient:adminsecret@cloudnative-coffee-auth-server.cfapps.io/oauth/token -d grant_type=client_credentials

```

## Note

This is not production ready. This is designed to simulate the OAuth token flow.


