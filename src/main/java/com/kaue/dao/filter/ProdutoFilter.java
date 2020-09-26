package com.kaue.dao.filter;

import com.kaue.model.Produto;

public class ProdutoFilter extends GenericFilter {
	
	private Produto produto = new Produto();

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

}
