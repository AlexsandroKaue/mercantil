package com.kaue.service;

import java.util.List;

import com.kaue.dao.filter.CategoriaFilter;
import com.kaue.model.Categoria;

public interface CategoriaService {
	
	public void salvar(Categoria categoria);
	public void excluir(Long id);
	public List<Categoria> pesquisar(CategoriaFilter filtro);

}
