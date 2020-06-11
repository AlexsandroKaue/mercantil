package com.kaue.dao.custom;

import java.util.List;

import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.model.Produto;

public interface ProdutoRepositoryCustom {

	public List<Produto> findProdutoByFiltro(ProdutoFilter produtoFiltro);
	public Long countProdutoByFiltro(ProdutoFilter produtoFiltro);
}
