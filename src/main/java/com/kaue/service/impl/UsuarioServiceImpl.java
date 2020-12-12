package com.kaue.service.impl;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import com.kaue.dao.UsuarioDAO;
import com.kaue.dao.filter.UsuarioFilter;
import com.kaue.model.Usuario;
import com.kaue.service.UsuarioService;
import com.kaue.util.FileManager;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@Autowired
    ResourceLoader resourceLoader;
	
	@Autowired
    FileManager fileManager;

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

	@Override
	public String salvarImagem(MultipartFile file, String nome) throws Exception {
		return fileManager.salvarImagem(file, nome, "classpath:static/custom/img/usuario");
	}

	@Override
	public String carregarImagem(String nome) {
		return fileManager.carregarImagem(nome, "classpath:static/custom/img/usuario");
	}

	@Transactional
	public Long obterMaxId() {
		return usuarioDAO.obterMaxId();
	}
	
	
}
