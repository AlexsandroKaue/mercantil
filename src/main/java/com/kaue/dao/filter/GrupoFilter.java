package com.kaue.dao.filter;

import com.kaue.model.Grupo;

public class GrupoFilter extends GenericFilter {
	
	private Grupo grupo = new Grupo();

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	
}
