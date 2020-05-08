package com.kaue.service;

import java.util.List;

import com.kaue.dao.filter.LoteFilter;
import com.kaue.model.Lote;

public interface LoteService {
	
	public void salvar(Lote lote);
	public void excluir(Long id);
	public List<Lote> pesquisar(LoteFilter filtro);

}
