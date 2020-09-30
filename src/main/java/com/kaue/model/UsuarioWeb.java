package com.kaue.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UsuarioWeb extends User{
	
	private static final long serialVersionUID = 1L;
	
	private String nome;
	
	private Usuario usuario;
	
	public UsuarioWeb(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getLogin(), usuario.getSenha(), authorities);
		
		this.nome = usuario.getNome();
		this.usuario = usuario;
	}
	
	public String getNome() {
		return nome;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	
}
