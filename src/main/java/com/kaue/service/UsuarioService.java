package com.kaue.service;

import java.util.List;

import com.kaue.dao.filter.UsuarioFilter;
import com.kaue.model.Usuario;

public interface UsuarioService {
	
	public Usuario salvar(Usuario usuario);
	public void excluir(Long id);
	public List<Usuario> pesquisar(UsuarioFilter filtro);
	public Long contar(UsuarioFilter filtro);
	public Usuario buscarPorId(Long id);
	public Usuario buscarPorLogin(String login);
}
