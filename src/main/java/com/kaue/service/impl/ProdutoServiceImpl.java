package com.kaue.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	public void salvar(Produto produto) {
		produtoDAO.save(produto);
	}

	@Override
	public void excluir(Long codigo) {
		produtoDAO.deleteById(codigo);
	}

	@Override
	public List<Produto> pesquisar(ProdutoFilter filtro) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
