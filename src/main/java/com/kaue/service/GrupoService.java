package com.kaue.service;

import java.util.List;

import com.kaue.dao.filter.GrupoFilter;
import com.kaue.dao.filter.UsuarioFilter;
import com.kaue.model.Grupo;
import com.kaue.model.Usuario;

public interface GrupoService {
	
	public Grupo salvar(Grupo grupo);
	public void excluir(Long id);
	public List<Grupo> pesquisar(GrupoFilter filtro);
	public Long contar(GrupoFilter filtro);
	public Grupo buscarPorId(Long id);
	public List<Grupo> buscarPorUsuario(Usuario usuario);

}
