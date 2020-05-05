package com.kaue.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaue.dao.EnderecoDAO;
import com.kaue.dao.filter.EnderecoFilter;
import com.kaue.model.Endereco;
import com.kaue.service.EnderecoService;

@Service
public class EnderecoServiceImpl implements EnderecoService{

	@Autowired
	private EnderecoDAO enderecoDAO;

	@Override
	public void salvar(Endereco endereco) {
		enderecoDAO.save(endereco);
	}

	@Override
	public void excluir(Long id) {
		enderecoDAO.deleteById(id);
	}

	@Override
	public List<Endereco> pesquisar(EnderecoFilter filtro) {
		return null;
	}
	
	
}
