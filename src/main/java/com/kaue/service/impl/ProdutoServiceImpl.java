package com.kaue.service.impl;

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
	public void salvar(Produto produto) {
		try {
			produtoDAO.save(produto);
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
		String descricao = (filtro.getDescricao() == null ? "" : filtro.getDescricao());
		return produtoDAO.findByDescricaoContainingIgnoreCaseOrderByIdDesc(descricao);
	}

	@Override
	public Produto buscarPorId(Long id) {
		return produtoDAO.findById(id).orElse(null);
	}
	
	
}
