package com.kaue.service;

import java.util.List;

import com.kaue.dao.filter.PermissaoFilter;
import com.kaue.model.Grupo;
import com.kaue.model.Permissao;

public interface PermissaoService {
	
	public void salvar(Permissao permissao);
	public void excluir(Long id);
	public List<Permissao> pesquisar(PermissaoFilter filtro);
	public List<Permissao> buscarPorGrupo(Grupo grupo);

}
