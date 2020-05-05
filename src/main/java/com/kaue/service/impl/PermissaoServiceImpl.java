package com.kaue.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaue.dao.PermissaoDAO;
import com.kaue.dao.filter.PermissaoFilter;
import com.kaue.model.Permissao;
import com.kaue.service.PermissaoService;

@Service
public class PermissaoServiceImpl implements PermissaoService{

	@Autowired
	private PermissaoDAO permissaoDAO;
	
	public void salvar(Permissao permissao) {
		permissaoDAO.save(permissao);
	}
	
	public void excluir(Long id) {
		permissaoDAO.deleteById(id);
	}
	
	public List<Permissao> pesquisar(PermissaoFilter filtro) {
		String descricao = (filtro.getDescricao() == null ? "" : filtro.getDescricao());
		return permissaoDAO.findByDescricaoContainingIgnoreCase(descricao);
	}
}
