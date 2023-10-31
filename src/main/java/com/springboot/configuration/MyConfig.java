package com.springboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig{
	
	
		
		@Bean
		public UserDetailsService getUserDetailsService() {
			return new UserDetailServiceImpl();
		}
		
		@Bean
		public BCryptPasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		
		@Bean
		public DaoAuthenticationProvider daoAuthenticationProvider() {
			DaoAuthenticationProvider AuthenticationProvider = new DaoAuthenticationProvider();
			AuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
			AuthenticationProvider.setPasswordEncoder(passwordEncoder());
			
			return AuthenticationProvider;
		} 
		
		//configure method..
		@Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
			
			http.authorizeHttpRequests((authz) -> authz
	                .requestMatchers("/admin/**").hasRole("ADMIN")
	                .requestMatchers("/user/**").hasRole("USER")
	                .requestMatchers("/**").permitAll()
	                .anyRequest().authenticated()
	                )
			
				.csrf(csrf->csrf
					.disable())
				
				.formLogin(form -> form
						.loginPage("/login")
						.defaultSuccessUrl("/user/home")
		                .permitAll()
		            );
			       

			http.authenticationProvider(daoAuthenticationProvider());
			
			DefaultSecurityFilterChain defaultSecurityFilterChain = http.build();
			
			return defaultSecurityFilterChain;
		}
		
	
}
