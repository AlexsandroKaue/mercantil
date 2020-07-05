package com.kaue.service;

import java.util.List;

import com.kaue.dao.filter.ClienteFilter;
import com.kaue.model.Cliente;

public interface ClienteService {
	
	public Cliente salvar(Cliente cliente);
	public void excluir(Long id);
	public List<Cliente> pesquisar(ClienteFilter filtro);
	public Long contar(ClienteFilter filtro);
	public Cliente buscarPorId(Long id);
}
