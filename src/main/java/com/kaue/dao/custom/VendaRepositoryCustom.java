package com.kaue.dao.custom;

import java.util.List;

import com.kaue.dao.filter.CategoriaFilter;
import com.kaue.dao.filter.VendaFilter;
import com.kaue.model.Categoria;
import com.kaue.model.Venda;

public interface VendaRepositoryCustom {
	
	public List<Venda> findVendaByFiltro(VendaFilter vendaFiltro);
	public Long countVendaByFiltro(VendaFilter vendaFiltro);

}
