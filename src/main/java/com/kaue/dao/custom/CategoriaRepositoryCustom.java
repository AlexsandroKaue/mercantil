package com.kaue.dao.custom;

import java.util.List;

import com.kaue.dao.filter.CategoriaFilter;
import com.kaue.model.Categoria;

public interface CategoriaRepositoryCustom {
	
	public List<Categoria> findCategoriaByFiltro(CategoriaFilter categoriaFiltro);
	public Long countCategoriaByFiltro(CategoriaFilter categoriaFiltro);

}
