package com.kaue.enumeration;

public enum TipoConta {

	COFRE("Cofre Pessoal"),
	OUTROS("Outros");
	
	private String descricao;
	
	private TipoConta(String descricao) {
		this.descricao = descricao;
	}
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}
