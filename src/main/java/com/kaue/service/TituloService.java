package com.kaue.service;

import java.util.List;

import com.kaue.dao.filter.TituloFilter;
import com.kaue.model.Titulo;

public interface TituloService {
	
	public void salvar(Titulo titulo);
	public void excluir(Long codigo);
	public List<Titulo> pesquisar(TituloFilter filtro);
	public String receber(Long codigo);
}
