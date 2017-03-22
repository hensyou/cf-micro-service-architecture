package com.cloudnativecoffee.market;

import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;

public class ClientDetailsServiceConfig extends ClientDetailsServiceConfigurer {

	public ClientDetailsServiceConfig(ClientDetailsServiceBuilder<?> builder) {
		super(builder);
	}
//
//	@Override
//	public void configure(ClientDetailsServiceBuilder<?> builder)
//			throws Exception {
//		builder.clients(clientDetailsService)
//	}
	
	

}
