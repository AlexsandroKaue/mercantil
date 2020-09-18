package com.kaue.dao.filter;

import com.kaue.model.Categoria;

public class CategoriaFilter extends GenericFilter{
	
	private String descricao;
	
	private Categoria categoria = new Categoria();

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
}
