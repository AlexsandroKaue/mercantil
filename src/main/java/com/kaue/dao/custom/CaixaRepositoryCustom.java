package com.kaue.dao.custom;

import java.util.List;

import com.kaue.dao.filter.CaixaFilter;
import com.kaue.model.Caixa;

public interface CaixaRepositoryCustom {
	
	public List<Caixa> findCaixaByFiltro(CaixaFilter caixaFiltro);
	public Long countCaixaByFiltro(CaixaFilter caixaFiltro);
	public Caixa obterCaixaMaisRecente();

}
