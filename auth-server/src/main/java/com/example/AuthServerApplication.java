package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@SpringBootApplication
public class AuthServerApplication{

	
	
	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}
	
	@Configuration
	protected static class LoginConfig extends WebSecurityConfigurerAdapter {
		
		@Autowired
		private AuthenticationManager authenticationManager;
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.formLogin()
			//.loginPage("/login")
			.permitAll()
			.and()
			.authorizeRequests()
			.anyRequest().
			authenticated();
		}
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.parentAuthenticationManager(authenticationManager);
		}
	}
	
	@Configuration
	@EnableAuthorizationServer
	protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {
		
		@Autowired
		private AuthenticationManager authenticationManager;
		
		@Override
	    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
	        endpoints.tokenStore(tokenStore())
	                 .accessTokenConverter(accessTokenConverter())
	                 .authenticationManager(authenticationManager);
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
	        defaultTokenServices.setSupportRefreshToken(true);
	        return defaultTokenServices;
	    }
	    
	    @Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.inMemory()
					.withClient("adminclient")
					.secret("adminsecret")
					.authorizedGrantTypes("authorization_code").scopes("read","write");
					//.authorizedGrantTypes("authorization_code", "client_credentials", "refresh_token", "password").scopes("read","write");
		}
	}
	
	
}
