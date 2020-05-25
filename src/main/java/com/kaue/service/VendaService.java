package com.kaue.service;

import java.util.List;

import com.kaue.dao.filter.VendaFilter;
import com.kaue.model.Venda;

public interface VendaService {

	public void salvar(Venda venda);
	public void excluir(Long codigo);
	public List<Venda> pesquisar(VendaFilter filtro);
	public Venda buscarPorId(Long id);
}
