package com.kaue.dao.filter;

import com.kaue.model.Usuario;

public class UsuarioFilter extends GenericFilter {
	
	private Usuario usuario = new Usuario();

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
