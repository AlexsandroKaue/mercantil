package com.kaue.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

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

	@Transactional
	public Usuario salvar(Usuario usuario) {
		return usuarioDAO.save(usuario);
	}

	@Transactional
	public void excluir(Long id) {
		usuarioDAO.deleteById(id);
	}

	@Transactional
	public List<Usuario> pesquisar(UsuarioFilter filtro) {
		List<Usuario> usuarioList = usuarioDAO.findUsuarioByFiltro(filtro);
		if(usuarioList==null) {
			usuarioList = new ArrayList<Usuario>();
		}
		return usuarioList;
	}
	
	@Transactional
	public Long contar(UsuarioFilter filtro) {
		Long count = usuarioDAO.countUsuarioByFiltro(filtro);
		if(count!=null) {
			return count;
		}
		return 0L;
	}

	@Transactional
	public Usuario buscarPorId(Long id) {
		return usuarioDAO.findById(id).orElse(null);
	}

	@Override
	public Usuario buscarPorLogin(String login) {
		return usuarioDAO.findByLogin(login);
	}
	
}
