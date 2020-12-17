package com.kaue.enumeration;

public enum Unitario {
	
	Un("Unidade - Un", "Un"),
	Pc("Pacote Fechado- Pc", "Pc"),
	Kg("Quilo - Kg", "Kg");
	
	private String descricao;
	
	private String sigla;
	
	Unitario(String descricao, String sigla) {
		this.descricao = descricao;
		this.sigla = sigla;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}
