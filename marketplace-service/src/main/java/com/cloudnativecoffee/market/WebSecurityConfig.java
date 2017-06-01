package com.cloudnativecoffee.market;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
/**
 * Provides security configuration for the application
 * @author lshannon
 *
 */
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
	        .authorizeRequests()
			.antMatchers("/").permitAll()
	        .antMatchers("/hystrix.stream/**").permitAll()
	        .antMatchers("/webjars/**").permitAll()
			.antMatchers("/home").permitAll()
			.antMatchers("/images/**").permitAll()
	        .anyRequest().authenticated()
	        .and().exceptionHandling();
    }
}  


