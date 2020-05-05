package com.kaue.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class ProdutoFornecedor {
	
	@EmbeddedId
	private ProdutoFornecedorCK key;
	
	@ManyToOne
	@MapsId(value = "codigoProduto")
	private Produto produto;
	
	@ManyToOne
	@MapsId(value = "codigoFornecedor")
	private Fornecedor fornecedor;

	public ProdutoFornecedorCK getKey() {
		return key;
	}

	public void setKey(ProdutoFornecedorCK key) {
		this.key = key;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
}
