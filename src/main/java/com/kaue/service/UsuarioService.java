package com.kaue.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kaue.dao.filter.UsuarioFilter;
import com.kaue.model.Usuario;

public interface UsuarioService {
	
	public Usuario salvar(Usuario usuario);
	public void excluir(Long id);
	public List<Usuario> pesquisar(UsuarioFilter filtro);
	public Long contar(UsuarioFilter filtro);
	public Usuario buscarPorId(Long id);
	public Usuario buscarPorLogin(String login);
	public String salvarImagem(MultipartFile file, String nome) throws Exception;
	public String carregarImagem(String nome);
	public Long obterIdAtual();
}
