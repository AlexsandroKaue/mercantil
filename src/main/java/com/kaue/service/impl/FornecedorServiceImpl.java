package com.kaue.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaue.dao.FornecedorDAO;
import com.kaue.dao.filter.FornecedorFilter;
import com.kaue.model.Fornecedor;
import com.kaue.service.FornecedorService;

@Service
public class FornecedorServiceImpl implements FornecedorService{

	@Autowired
	private FornecedorDAO fornecedorDAO;

	@Override
	public void salvar(Fornecedor fornecedor) {
		fornecedorDAO.save(fornecedor);
	}

	@Override
	public void excluir(Long id) {
		fornecedorDAO.deleteById(id);
	}

	@Override
	public List<Fornecedor> pesquisar(FornecedorFilter filtro) {
		return null;
	}
	
	
}
