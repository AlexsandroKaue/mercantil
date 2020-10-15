package com.kaue.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaue.dao.MovimentacaoCaixaDAO;
import com.kaue.dao.filter.MovimentacaoCaixaFilter;
import com.kaue.model.MovimentacaoCaixa;
import com.kaue.service.MovimentacaoCaixaService;

@Service
public class MovimentacaoCaixaServiceImpl implements MovimentacaoCaixaService {
	
	@Autowired
	private MovimentacaoCaixaDAO movimentacaoCaixaDAO;

	@Transactional
	public MovimentacaoCaixa salvar(MovimentacaoCaixa movimentacaoCaixa) throws Exception {
		try {
			return movimentacaoCaixaDAO.save(movimentacaoCaixa);
		} catch(Exception e) {
			throw new Exception("Erro ao tentar salvar a movimentação");
		}
		
	}

	public List<MovimentacaoCaixa> pesquisar(MovimentacaoCaixaFilter filtro) {
		return null;
	}
	
}
