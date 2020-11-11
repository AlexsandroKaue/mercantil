package com.kaue.dao.custom;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.model.Produto;

public interface ProdutoRepositoryCustom {

	public List<Produto> findProdutoByFiltro(ProdutoFilter produtoFiltro);
	public Page<Produto> findProdutoByFiltro(ProdutoFilter produtoFiltro, Pageable pageable);
	public Long countProdutoByFiltro(ProdutoFilter produtoFiltro);
	public Long obterMaxId();
}
