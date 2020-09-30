package com.kaue.dao.filter;

import com.kaue.model.Caixa;

public class CaixaFilter extends GenericFilter {
	
	private Caixa caixa;

	public CaixaFilter() {
		caixa = new Caixa();
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

}
