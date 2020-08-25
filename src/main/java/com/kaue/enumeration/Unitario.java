package com.kaue.enumeration;

public enum Unitario {
	
	Un("Unidade - Un"),
	Pc("Pacote - Pc"),
	Kg("Quilo - Kg");
	
	private String descricao;
	
	Unitario(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

}
