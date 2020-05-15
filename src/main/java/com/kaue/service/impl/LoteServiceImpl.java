package com.kaue.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kaue.dao.LoteDAO;
import com.kaue.dao.filter.LoteFilter;
import com.kaue.model.Lote;
import com.kaue.service.LoteService;
import com.kaue.service.ProdutoService;

@Service
public class LoteServiceImpl implements LoteService{
	
	@Autowired
	private LoteDAO loteDAO;
	
	@Autowired
	private ProdutoService produtoService;

	@Transactional
	public void salvar(Lote lote) {
		loteDAO.save(lote);
	}

	@Override
	public void excluir(Long id) {
		loteDAO.deleteById(id);
	}

	@Override
	public List<Lote> pesquisar(LoteFilter filtro) {
		// TODO Auto-generated method stub
		List<Order> orderable = new ArrayList<Order>();
		orderable.add(new Order(Direction.DESC, "id"));
		Sort sorter = Sort.by(orderable);
		return loteDAO.findAllAndSort(sorter);
	}

	@Override
	public Lote buscarPorId(Long id) {
		return loteDAO.findById(id).orElse(null);
	}
	
}
