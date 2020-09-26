package com.kaue.dao.custom;

import java.util.List;

import com.kaue.dao.filter.FornecedorFilter;
import com.kaue.model.Fornecedor;

public interface FornecedorRepositoryCustom {
	
	public List<Fornecedor> findFornecedorByFiltro(FornecedorFilter fornecedorFiltro);
	public Long countFornecedorByFiltro(FornecedorFilter fornecedorFiltro);
	
}
