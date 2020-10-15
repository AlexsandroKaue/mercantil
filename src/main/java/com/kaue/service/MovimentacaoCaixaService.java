package com.kaue.service;

import java.util.List;

import com.kaue.dao.filter.MovimentacaoCaixaFilter;
import com.kaue.model.MovimentacaoCaixa;

public interface MovimentacaoCaixaService {
	
	public MovimentacaoCaixa salvar(MovimentacaoCaixa movimentacaoCaixa) throws Exception;
	public List<MovimentacaoCaixa> pesquisar(MovimentacaoCaixaFilter filtro);

}
