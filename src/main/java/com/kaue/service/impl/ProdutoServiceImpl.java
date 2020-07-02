package com.kaue.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.kaue.dao.ProdutoDAO;
import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.model.Produto;
import com.kaue.service.ProdutoService;

@Service
public class ProdutoServiceImpl implements ProdutoService{

	@Autowired
	private ProdutoDAO produtoDAO;

	@Override
	public Produto salvar(Produto produto) {
		try {
			return produtoDAO.save(produto);
		} catch(DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato de data inválido");
		}
	}

	@Override
	public void excluir(Long codigo) {
		produtoDAO.deleteById(codigo);
	}

	@Override
	public List<Produto> pesquisar(ProdutoFilter filtro) {
		List<Produto> produtoList = produtoDAO.findProdutoByFiltro(filtro);
		if(produtoList==null) {
			produtoList = new ArrayList<Produto>();
		}
		return produtoList;
	}
	
	@Override
	public Long contar(ProdutoFilter filtro) {
		Long count = produtoDAO.countProdutoByFiltro(filtro);
		if(count!=null) {
			return count;
		}
		return 0L;
	}

	@Override
	public Produto buscarPorId(Long id) {
		return produtoDAO.findById(id).orElse(null);
	}

	@Override
	public Produto buscarPorCodigo(String codigo) {
		String cod = (codigo == null ? "" : codigo);
		return produtoDAO.findByCodigo(cod);
	}
	
}
