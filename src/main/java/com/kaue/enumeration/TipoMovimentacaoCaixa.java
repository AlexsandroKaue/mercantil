package com.kaue.enumeration;

public enum TipoMovimentacaoCaixa {

	SANGRIA("Sangria - retirada de dinheiro");
	
	private String descricao;
	
	private TipoMovimentacaoCaixa(String descricao) {
		this.descricao = descricao;
	}
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}
