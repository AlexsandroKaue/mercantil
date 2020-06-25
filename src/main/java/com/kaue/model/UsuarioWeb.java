package com.kaue.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


public class UsuarioWeb extends User{
	
	private static final long serialVersionUID = 1L;
	
	private String nome;
	
	public UsuarioWeb(String nome, String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
}
