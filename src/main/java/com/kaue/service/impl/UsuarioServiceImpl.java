package com.kaue.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.kaue.dao.UsuarioDAO;
import com.kaue.dao.filter.UsuarioFilter;
import com.kaue.model.Usuario;
import com.kaue.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired
	private UsuarioDAO usuarioDAO;

	@Override
	public Usuario salvar(Usuario usuario) {
		try {
			return usuarioDAO.save(usuario);
		} catch(DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato de data inválido");
		}
	}

	@Override
	public void excluir(Long id) {
		usuarioDAO.deleteById(id);
	}

	@Override
	public List<Usuario> pesquisar(UsuarioFilter filtro) {
		List<Usuario> usuarioList = usuarioDAO.findUsuarioByFiltro(filtro);
		if(usuarioList==null) {
			usuarioList = new ArrayList<Usuario>();
		}
		return usuarioList;
	}
	
	@Override
	public Long contar(UsuarioFilter filtro) {
		Long count = usuarioDAO.countUsuarioByFiltro(filtro);
		if(count!=null) {
			return count;
		}
		return 0L;
	}

	@Override
	public Usuario buscarPorId(Long id) {
		return usuarioDAO.findById(id).orElse(null);
	}
	
}
