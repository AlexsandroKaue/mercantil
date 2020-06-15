package com.kaue.enumeration;

public enum StatusUsuario {
	
	SIM("Sim"),
	NAO("Não");
	
	private String descricao;

	private StatusUsuario(String descricao) {
		this.setDescricao(descricao);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
