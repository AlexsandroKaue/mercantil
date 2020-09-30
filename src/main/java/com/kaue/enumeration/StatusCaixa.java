package com.kaue.enumeration;

public enum StatusCaixa {
	
	ABERTO("Aberto"),
	FECHADO("Fechado");
	
	private String descricao;

	private StatusCaixa(String descricao) {
		this.setDescricao(descricao);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
