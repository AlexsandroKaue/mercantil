package com.kaue.dao.custom;

import java.util.List;

import com.kaue.dao.filter.CadernetaFilter;
import com.kaue.model.Caderneta;

public interface CadernetaRepositoryCustom {
	
	public List<Caderneta> findCadernetaByFiltro(CadernetaFilter cadernetaFiltro);
	public Long countCadernetaByFiltro(CadernetaFilter cadernetaFiltro);

}
