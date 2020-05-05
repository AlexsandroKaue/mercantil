package com.kaue.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
class ProdutoFornecedorCK implements Serializable{
	
	private static final long serialVersionUID = 5089725270434001457L;

	private Long codigoProduto;
	
	private Long codigoFornecedor;

	public Long getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(Long codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public Long getCodigoFornecedor() {
		return codigoFornecedor;
	}

	public void setCodigoFornecedor(Long codigoFornecedor) {
		this.codigoFornecedor = codigoFornecedor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoFornecedor == null) ? 0 : codigoFornecedor.hashCode());
		result = prime * result + ((codigoProduto == null) ? 0 : codigoProduto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoFornecedorCK other = (ProdutoFornecedorCK) obj;
		if (codigoFornecedor == null) {
			if (other.codigoFornecedor != null)
				return false;
		} else if (!codigoFornecedor.equals(other.codigoFornecedor))
			return false;
		if (codigoProduto == null) {
			if (other.codigoProduto != null)
				return false;
		} else if (!codigoProduto.equals(other.codigoProduto))
			return false;
		return true;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
