package com.kaue.dao.custom;

import java.util.List;

import com.kaue.dao.filter.GrupoFilter;
import com.kaue.model.Grupo;

public interface GrupoRepositoryCustom {

	public List<Grupo> findGrupoByFiltro(GrupoFilter grupoFiltro);
	public Long countGrupoByFiltro(GrupoFilter grupoFiltro);
}
