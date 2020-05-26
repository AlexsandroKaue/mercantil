package com.kaue.service;

import java.util.List;

import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.model.Produto;

public interface ProdutoService {

	public void salvar(Produto produto);
	public void excluir(Long codigo);
	public List<Produto> pesquisar(ProdutoFilter filtro);
	public Produto buscarPorCodigo(String codigo);
	public Produto buscarPorId(Long id);
}
