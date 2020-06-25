package com.kaue.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaue.dao.GrupoDAO;
import com.kaue.dao.filter.GrupoFilter;
import com.kaue.model.Grupo;
import com.kaue.model.Usuario;
import com.kaue.service.GrupoService;

@Service
public class GrupoServiceImpl implements GrupoService{
	
	@Autowired
	private GrupoDAO grupoDAO;

	@Override
	public void salvar(Grupo grupo) {
		grupoDAO.save(grupo);
	}

	@Override
	public void excluir(Long id) {
		grupoDAO.deleteById(id);
	}

	@Override
	public List<Grupo> pesquisar(GrupoFilter filtro) {
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		return grupoDAO.findByDescricaoContainingIgnoreCaseOrderByIdDesc(descricao);
	}

	@Override
	public List<Grupo> buscarPorUsuario(Usuario usuario) {
		return null;
	}

	
	
}
