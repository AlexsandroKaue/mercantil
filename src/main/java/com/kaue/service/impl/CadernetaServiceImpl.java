package com.kaue.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaue.dao.CadernetaDAO;
import com.kaue.dao.filter.CadernetaFilter;
import com.kaue.model.Caderneta;
import com.kaue.service.CadernetaService;

@Service
public class CadernetaServiceImpl implements CadernetaService{
	
	@Autowired
	private CadernetaDAO cadernetaDAO;

	@Override
	public Caderneta buscarPorId(Long id) {
		return cadernetaDAO.findById(id).orElse(null);
	}
	
	@Override
	public Caderneta salvar(Caderneta caderneta) {
		return cadernetaDAO.save(caderneta);
	}

	@Override
	public void excluir(Long id) {
		cadernetaDAO.deleteById(id);
	}

	@Override
	public List<Caderneta> pesquisar(CadernetaFilter filtro) {
		List<Caderneta> cadernetaList = cadernetaDAO.findCadernetaByFiltro(filtro);
		if(cadernetaList==null) {
			cadernetaList = new ArrayList<Caderneta>();
		}
		return cadernetaList;
	}

	@Override
	public Caderneta buscarPorCliente(Long clienteId) {
		return cadernetaDAO.findByClienteId(clienteId);
	}
	
}
