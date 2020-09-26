package com.kaue.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaue.dao.FornecedorDAO;
import com.kaue.dao.filter.FornecedorFilter;
import com.kaue.model.Cliente;
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
		List<Fornecedor> fornecedorList = fornecedorDAO.findFornecedorByFiltro(filtro);
		if(fornecedorList==null) {
			fornecedorList = new ArrayList<Fornecedor>();
		}
		return fornecedorList;
	}
	
	
}
