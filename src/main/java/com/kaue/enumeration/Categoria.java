package com.kaue.enumeration;

public enum Categoria {
	HIGIENE_PESSOAL("Higiene pessoal"),
	ALIMENTACAO("Alimentação"),
	PRODUTO_DE_LIMPEZA("Produto de limpeza"),
	PAPELARIA("Papelaria");
	
	private String descricao;
	
	private Categoria(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}
