package com.kaue.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
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
        	.antMatchers("/caixa/**").hasAuthority("OPERAR_CAIXA")
        	
        	.antMatchers("/categorias").hasAuthority("CONSULTA_CATEGORIA")
        	.antMatchers("/categorias/novo").hasAuthority("INCLUIR_CATEGORIA")
        	.antMatchers("/categorias/**").hasAuthority("ALTERAR_CATEGORIA")
        	
        	.antMatchers("/fornecedores").hasAuthority("CONSULTA_FORNECEDOR")
        	.antMatchers("/fornecedores/novo").hasAuthority("INCLUIR_FORNECEDOR")
        	.antMatchers("/fornecedores/**").hasAuthority("ALTERAR_FORNECEDOR")
        	
        	.antMatchers("/grupos").hasAuthority("CONSULTA_GRUPO")
        	.antMatchers("/grupos/novo").hasAuthority("INCLUIR_GRUPO")
        	.antMatchers("/grupos/**").hasAuthority("ALTERAR_GRUPO")
        	
        	.antMatchers("/lotes").hasAuthority("CONSULTA_LOTE")
        	.antMatchers("/lotes/novo").hasAuthority("INCLUIR_LOTE")
        	.antMatchers("/lotes/**").hasAuthority("ALTERAR_LOTE")
        	
        	.antMatchers("/permissoes").hasAuthority("CONSULTA_PERMISSAO")
        	.antMatchers("/permissoes/novo").hasAuthority("INCLUIR_PERMISSAO")
        	.antMatchers("/permissoes/**").hasAuthority("ALTERAR_PERMISSAO")
        	
        	.antMatchers("/produtos").hasAuthority("CONSULTA_PRODUTO")
        	.antMatchers("/produtos/novo").hasAuthority("INCLUIR_PRODUTO")
        	.antMatchers("/produtos/**").hasAuthority("ALTERAR_PRODUTO")
        	
        	.antMatchers("/usuarios").hasAuthority("CONSULTA_USUARIO")
        	.antMatchers("/usuarios/novo").hasAuthority("INCLUIR_USUARIO")
        	.antMatchers("/usuarios/**").hasAuthority("ALTERAR_USUARIO")
        	
        	.antMatchers("/clientes").hasAuthority("CONSULTA_CLIENTE")
        	.antMatchers("/clientes/novo").hasAuthority("INCLUIR_CLIENTE")
        	.antMatchers("/clientes/**").hasAuthority("ALTERAR_CLIENTE")
            // Para qualquer requisição (anyRequest) é preciso estar 
            // autenticado (authenticated).
            .anyRequest().authenticated()
        .and()
        .formLogin()
        // Aqui dizemos que temos uma página customizada.
        .loginPage("/login") 
        // Mesmo sendo a página de login, precisamos avisar
        // ao Spring Security para liberar o acesso a ela.
        .defaultSuccessUrl("/inicio",true)
        .failureUrl("/login-error")
    	.permitAll()
    	.and()
        .logout()
        .logoutUrl("/logout")
        .deleteCookies("JSESSIONID")
        .and()
        .exceptionHandling().accessDeniedPage("/acessoNaoPermitido")
        .and()
        .rememberMe(); // <<< Habilita a função de "lembrar-me".; 
    
  }
  
  @Override
  protected void configure(AuthenticationManagerBuilder builder) throws Exception {
    builder.userDetailsService(comercialUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
  }
  
 
}