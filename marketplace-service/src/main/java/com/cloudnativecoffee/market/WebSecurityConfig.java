package com.cloudnativecoffee.market;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
	        .authorizeRequests()
	        .antMatchers("/hystrix.stream/**").permitAll()
	        .anyRequest().authenticated()
	        .and()
	        .formLogin().permitAll()
	        .defaultSuccessUrl("/");
    }
}

//class HystrixRequestContextServletFilter implements Filter {
//
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
//     throws IOException, ServletException {
//        HystrixRequestContext context = HystrixRequestContext.initializeContext();
//        try {
//            chain.doFilter(request, response);
//        } finally {
//            context.shutdown();
//        }
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void destroy() {
//        // TODO Auto-generated method stub
//
//    }
//}   


