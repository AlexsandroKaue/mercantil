package com.kaue.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaue.dao.CategoriaDAO;
import com.kaue.dao.FornecedorDAO;
import com.kaue.dao.filter.CategoriaFilter;
import com.kaue.dao.filter.FornecedorFilter;
import com.kaue.model.Categoria;
import com.kaue.model.Fornecedor;
import com.kaue.service.CategoriaService;
import com.kaue.service.FornecedorService;

@Service
public class CategoriaServiceImpl implements CategoriaService{
	
	@Autowired
	private CategoriaDAO categoriaDAO;

	@Override
	public void salvar(Categoria categoria) {
		categoriaDAO.save(categoria);
	}

	@Override
	public void excluir(Long id) {
		categoriaDAO.deleteById(id);
	}

	@Override
	public List<Categoria> pesquisar(CategoriaFilter filtro) {
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		return categoriaDAO.findByDescricaoContainingIgnoreCaseOrderByIdDesc(descricao);
	}

	
	
}
