package com.kaue.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityWebConfig extends WebSecurityConfigurerAdapter {
   
	@Autowired
	private ComercialUserDetailsService comercialUserDetailsService;  
	
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        	.antMatchers("/dist/**", "/plugins/**").permitAll()
            // Para qualquer requisição (anyRequest) é preciso estar 
            // autenticado (authenticated).
            .anyRequest().authenticated()
        .and()
        .formLogin()
        // Aqui dizemos que temos uma página customizada.
        .loginPage("/login") 
        // Mesmo sendo a página de login, precisamos avisar
        // ao Spring Security para liberar o acesso a ela.
        .defaultSuccessUrl("/usuarios",true)
        .failureUrl("/login-error")
    	.permitAll()
    	.and()
        .logout()
        .logoutUrl("/logout")
        .deleteCookies("JSESSIONID")
    	.and()
        .rememberMe(); // <<< Habilita a função de "lembrar-me".; 
    
  }
  
  @Override
  protected void configure(AuthenticationManagerBuilder builder) throws Exception {
    builder
        .userDetailsService(comercialUserDetailsService)
        .passwordEncoder(new BCryptPasswordEncoder());
	/* System.out.println(new BCryptPasswordEncoder().encode("123")); */
  }
  
 
}