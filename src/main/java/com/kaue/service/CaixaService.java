package com.kaue.service;

import java.util.List;

import com.kaue.dao.filter.CaixaFilter;
import com.kaue.model.Caixa;

public interface CaixaService {
	
	public Caixa salvar(Caixa caixa) throws Exception;
	public List<Caixa> pesquisar(CaixaFilter filtro);
	public Caixa obterCaixaMaisRecente();

}
