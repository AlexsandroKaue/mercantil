package com.kaue.dao.filter;

import com.kaue.model.Venda;

public class VendaFilter extends GenericFilter {
	
	private Venda venda = new Venda();

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}


}
