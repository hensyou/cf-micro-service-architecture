package com.cloudnativecoffee.order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EntityScan("com.cloudnativecoffee.model")
public class OrderServiceApplicationTests {

	@Test
	public void contextLoads() {
	}

}
