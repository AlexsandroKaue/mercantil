package com.kaue.service;

import java.util.List;

import com.kaue.dao.filter.GrupoFilter;
import com.kaue.model.Grupo;
import com.kaue.model.Usuario;

public interface GrupoService {
	
	public void salvar(Grupo grupo);
	public void excluir(Long id);
	public List<Grupo> pesquisar(GrupoFilter filtro);
	public List<Grupo> buscarPorUsuario(Usuario usuario);

}
