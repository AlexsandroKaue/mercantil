package com.kaue.dao.filter;

import com.kaue.model.Caderneta;

public class CadernetaFilter extends GenericFilter {
	
	private String descricao;
	
	private Caderneta caderneta = new Caderneta();

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Caderneta getCaderneta() {
		return caderneta;
	}

	public void setCaderneta(Caderneta caderneta) {
		this.caderneta = caderneta;
	}
	
}
