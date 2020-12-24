package com.kaue.service;

import java.util.List;

import com.kaue.dao.filter.CadernetaFilter;
import com.kaue.model.Caderneta;

public interface CadernetaService {
	
	public void salvar(Caderneta caderneta);
	public void excluir(Long id);
	public List<Caderneta> pesquisar(CadernetaFilter filtro);

}
