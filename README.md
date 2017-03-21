# Cloud Native Developer Training Course
In this course we will create a simple Cloud Native Architecture. In completing this task key technologies and cloud native patterns will be reviewed.

# Prequities

Has completed the online self service training:

https://cdp-landing-page-demo.apps.stg.azr-cc-pcf.cloud.bns/

https://pcf-tutorial-demo.apps.stg.azr-cc-pcf.cloud.bns/

Has completed:
- CDP Kickoff Session
- PCF Developer Training

# Agenda

The course run 8 hours starting at 9am sharp and completing at 5pm.

1. Introductions and House Keeping - 10 minutes
2. Git and Gradle Overview - 45 minutes
3. Break - 10 minutes
4. Testing - 45 minutes
5. Break - 10 minutes
6. Accelerator Pipeline - 45 minutes
7. Lunch Break - 1 hours
8. Hands on Spring Cloud Native Workshop - 4 hours

## Key Technologies and Patterns Reviewed

Through out the course the following will be covered:

1. Spring Boot Overview
2. Gradle Quick Start
3. Testing in Spring Boot
4. Using Repositories To Work With Data
5. Externalizing Configurations and Working With Profiles
6. Review of Working With Data In PCF
7. Quick Overview of Working with OAuth2 with Spring Boot
8. Adding Fault Tolerance
9. Overview of Messaging In The Cloud

## What This Course Is Not

This course is designed to reinforce concepts already reviewed and learned and give a direction on where to go to learn more. This course is for developers who will be building and running applications on Pivotal Cloud Foundry.

## What We Are Building

![Architecture](/images/architecture.png)


# Set Up 

## Gradle

For this course Gradle will be the build tool and dependancy management tool of choice. Maven will not be an option.

First install Gradle on your machine:

https://gradle.org/install

In STS Gradle support needs to be added. At the time of writing this Gradle Buildship was not working with STS. Instead classic Gradle was installed.

Get the dashboard open.

![Architecture](/images/setup-1-find-dashboard.png)

Select the IDE Extensions

![Architecture](/images/setup-2-ide.png)

Select the Gradle Classic Extension

![Architecture](/images/setup-3-classic-gradle.png)

## Lombok

Lombok makes creating domain objects easy. Rather than coding all the boiler plate constructors and methods we normally need, with Lombok we can create JPA Entity Bean with everything we need like this:

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
To get Lombok running we need to do two things:

1. Install it into the IDE (https://projectlombok.org/download.html)
2. Add it as a Gradle dependancies: compileOnly "org.projectlombok:lombok:1.16.14"

## Postgres

One of our services is backed with a relation DB. This can be installed locally without much pain:

https://www.postgresql.org/download/

## Rabbit MQ

Our services use Rabbit MQ to communicate.

## PWS Account

To get the full Cloud Native experience, create an account on PWS for free to complete these labs. This is PCF running in AWS.

## PCF Dev

You can develop locally against PCF Dev. This will provide everything that is needed to perform the Labs locally with an experience similar that of PWS. It does require 6GB of memory to be free:

https://pivotal.io/pcf-dev
