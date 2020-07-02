package com.kaue.service.impl;

import java.util.ArrayList;
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
	public Grupo salvar(Grupo grupo) {
		return grupoDAO.save(grupo);
	}

	@Override
	public void excluir(Long id) {
		grupoDAO.deleteById(id);
	}

	@Override
	public List<Grupo> pesquisar(GrupoFilter filtro) {
		List<Grupo> grupoList = grupoDAO.findGrupoByFiltro(filtro);
		if(grupoList == null) {
			grupoList = new ArrayList<Grupo>();
		}
		return grupoList;
	}

	@Override
	public List<Grupo> buscarPorUsuario(Usuario usuario) {
		return null;
	}

	@Override
	public Long contar(GrupoFilter filtro) {
		Long count = grupoDAO.countGrupoByFiltro(filtro);
		if(count!=null) {
			return count;
		}
		return 0L;
	}

	@Override
	public Grupo buscarPorId(Long id) {
		return grupoDAO.findById(id).orElse(null);
	}

	
	
}
