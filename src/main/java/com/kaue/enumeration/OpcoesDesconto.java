package com.kaue.enumeration;

public enum OpcoesDesconto {
	
	ZERO(0.00, "0%"),
	TRES(0.03, "3%"),
	CINCO(0.05, "5%");
	
	private String descricao;
	
	private double numero;
	
	private OpcoesDesconto(double numero, String descricao) {
		this.setNumero(numero);
		this.setDescricao(descricao);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getNumero() {
		return numero;
	}

	public void setNumero(double numero) {
		this.numero = numero;
	}

}
