package com.kaue.dao.custom;

import java.util.List;

import com.kaue.dao.filter.ClienteFilter;
import com.kaue.model.Cliente;

public interface ClienteRepositoryCustom {

	public List<Cliente> findClienteByFiltro(ClienteFilter clienteFiltro);
	public Long countClienteByFiltro(ClienteFilter clienteFiltro);
	public Long obterMaxId();
}
