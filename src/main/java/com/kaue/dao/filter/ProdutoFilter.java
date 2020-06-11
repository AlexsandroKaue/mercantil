package com.kaue.dao.filter;

import java.math.BigDecimal;
import java.util.Date;

import com.kaue.model.Categoria;

public class ProdutoFilter {
	
	private String _type;
	
	private String term;
	
	private String q;
	
	private Long page;
	
	private Long pageSize;
	
	private String codigo;
	
	private String descricao;
	
	private Date dataDeFabricacao;
	
	private Date dataDeVencimento;
	
	private BigDecimal valorDeCusto;
	
	private BigDecimal valorDeVenda;
	
	private Categoria categoria;
	
	public ProdutoFilter() {
		this.codigo = null;
		this.descricao = null;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataDeFabricacao() {
		return dataDeFabricacao;
	}

	public void setDataDeFabricacao(Date dataDeFabricacao) {
		this.dataDeFabricacao = dataDeFabricacao;
	}

	public Date getDataDeVencimento() {
		return dataDeVencimento;
	}

	public void setDataDeVencimento(Date dataDeVencimento) {
		this.dataDeVencimento = dataDeVencimento;
	}

	public BigDecimal getValorDeCusto() {
		return valorDeCusto;
	}

	public void setValorDeCusto(BigDecimal valorDeCusto) {
		this.valorDeCusto = valorDeCusto;
	}

	public BigDecimal getValorDeVenda() {
		return valorDeVenda;
	}

	public void setValorDeVenda(BigDecimal valorDeVenda) {
		this.valorDeVenda = valorDeVenda;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		this.page = page;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String get_type() {
		return _type;
	}

	public void set_type(String _type) {
		this._type = _type;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

}
