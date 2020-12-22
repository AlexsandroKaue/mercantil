package com.kaue.enumeration;

public enum TipoVenda {

	ESPECIE("Em espécie"),
	CONTA("Na conta");
	
	private String descricao;

	private TipoVenda(String descricao) {
		this.setDescricao(descricao);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
