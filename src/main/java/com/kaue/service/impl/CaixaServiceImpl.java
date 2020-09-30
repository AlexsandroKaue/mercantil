package com.kaue.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaue.dao.CaixaDAO;
import com.kaue.dao.filter.CaixaFilter;
import com.kaue.model.Caixa;
import com.kaue.service.CaixaService;

@Service
public class CaixaServiceImpl implements CaixaService{
	
	@Autowired
	private CaixaDAO caixaDAO;

	@Transactional
	public Caixa salvar(Caixa caixa) throws Exception {
		return caixaDAO.save(caixa);
	}

	public List<Caixa> pesquisar(CaixaFilter filtro) {
		return null;
	}

	@Override
	public Caixa obterCaixaMaisRecente() {
		return caixaDAO.obterCaixaMaisRecente();
	}
	
}
