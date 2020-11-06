package com.kaue.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kaue.dao.filter.ClienteFilter;
import com.kaue.model.Cliente;

public interface ClienteService {
	
	public Cliente salvar(Cliente cliente);
	public void excluir(Long id);
	public List<Cliente> pesquisar(ClienteFilter filtro);
	public Long contar(ClienteFilter filtro);
	public Cliente buscarPorId(Long id);
	public String salvarImagem(MultipartFile file, String nome) throws Exception;
	public Long obterMaxId();
	public String carregarImagem(String nome);
}
