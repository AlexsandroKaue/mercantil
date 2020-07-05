package com.kaue.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.kaue.dao.ClienteDAO;
import com.kaue.dao.filter.ClienteFilter;
import com.kaue.model.Cliente;
import com.kaue.service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService{

	@Autowired
	private ClienteDAO clienteDAO;

	@Transactional
	public Cliente salvar(Cliente cliente) {
		try {
			return clienteDAO.save(cliente);
		} catch(DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato de data inválido");
		}
	}

	@Transactional
	public void excluir(Long id) {
		clienteDAO.deleteById(id);
	}

	@Transactional
	public List<Cliente> pesquisar(ClienteFilter filtro) {
		List<Cliente> clienteList = clienteDAO.findClienteByFiltro(filtro);
		if(clienteList==null) {
			clienteList = new ArrayList<Cliente>();
		}
		return clienteList;
	}
	
	@Transactional
	public Long contar(ClienteFilter filtro) {
		Long count = clienteDAO.countClienteByFiltro(filtro);
		if(count!=null) {
			return count;
		}
		return 0L;
	}

	@Transactional
	public Cliente buscarPorId(Long id) {
		return clienteDAO.findById(id).orElse(null);
	}
	
}
