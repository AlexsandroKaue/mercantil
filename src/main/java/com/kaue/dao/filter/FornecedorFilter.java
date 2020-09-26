package com.kaue.dao.filter;

import com.kaue.model.Fornecedor;

public class FornecedorFilter extends GenericFilter {
	
	private Fornecedor fornecedor = new Fornecedor();

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
}
