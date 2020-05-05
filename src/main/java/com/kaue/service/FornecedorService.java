package com.kaue.service;

import java.util.List;

import com.kaue.dao.filter.FornecedorFilter;
import com.kaue.model.Fornecedor;

public interface FornecedorService {
	
	public void salvar(Fornecedor fornecedor);
	public void excluir(Long id);
	public List<Fornecedor> pesquisar(FornecedorFilter filtro);

}
