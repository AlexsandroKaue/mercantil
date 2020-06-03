package com.kaue.enumeration;

public enum StatusVenda {
	
	ABERTA("Aberta"),
	FINALIZADA("Finalizada"),
	CANCELADA("Cancelada");
	
	private String descricao;
	
	private StatusVenda(String descricao) {
		this.setDescricao(descricao);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
