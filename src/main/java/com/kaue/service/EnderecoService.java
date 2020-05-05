package com.kaue.service;

import java.util.List;

import com.kaue.dao.filter.EnderecoFilter;
import com.kaue.model.Endereco;

public interface EnderecoService {
	
	public void salvar(Endereco endereco);
	public void excluir(Long id);
	public List<Endereco> pesquisar(EnderecoFilter filtro);

}
