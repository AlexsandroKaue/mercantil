package com.kaue.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.kaue.dao.VendaDAO;
import com.kaue.dao.filter.VendaFilter;
import com.kaue.model.Venda;
import com.kaue.service.VendaService;

@Service
public class VendaServiceImpl implements VendaService{

	@Autowired
	private VendaDAO vendaDAO;

	@Override
	public void salvar(Venda venda) {
		try {
			vendaDAO.save(venda);
		} catch(DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato de data inválido");
		}
	}

	@Override
	public void excluir(Long codigo) {
		vendaDAO.deleteById(codigo);
	}

	@Override
	public List<Venda> pesquisar(VendaFilter filtro) {
		String descricao = (filtro.getDescricao() == null ? "" : filtro.getDescricao());
		return vendaDAO.findByDescricaoContainingIgnoreCaseOrderByIdDesc(descricao);
	}

	@Override
	public Venda buscarPorId(Long id) {
		return vendaDAO.findById(id).orElse(null);
	}
	
	
}
