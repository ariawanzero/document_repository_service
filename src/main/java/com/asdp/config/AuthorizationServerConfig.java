package com.asdp.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager auth;
	
	@Autowired
	private DataSource dataSource;
	 
	@Bean
	public JdbcTokenStore tokenStore() {
	    return new JdbcTokenStore(dataSource);
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	 
	@Bean
	@Primary
	public AuthorizationServerTokenServices tokenServices() {
	    DefaultTokenServices tokenServices = new DefaultTokenServices();
	    tokenServices.setTokenStore(tokenStore());
	    
	    return tokenServices;
	}
     
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
				.tokenStore(tokenStore())
				.authenticationManager(auth); 		
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
		configurer
				.jdbc(dataSource)
				.passwordEncoder(passwordEncoder());
	}
}
