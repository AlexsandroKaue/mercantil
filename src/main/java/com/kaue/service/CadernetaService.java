package com.kaue.service;

import java.util.List;

import com.kaue.dao.filter.CadernetaFilter;
import com.kaue.model.Caderneta;
import com.kaue.model.Cliente;

public interface CadernetaService {
	
	public Caderneta buscarPorId(Long id);
	public Caderneta buscarPorCliente(Long clienteId);
	public Caderneta salvar(Caderneta caderneta);
	public void excluir(Long id);
	public List<Caderneta> pesquisar(CadernetaFilter filtro);

}
