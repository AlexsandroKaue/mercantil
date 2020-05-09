package com.kaue.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.NumberFormat;


@Entity
@Audited
public class Produto {
	
	@Id
	@SequenceGenerator(name = "seq_produto", sequenceName = "seq_produto", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "seq_produto", strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@NotEmpty(message = "Descrição é obrigatória")
	private String descricao;
	
	/*
	 * @NotNull(message = "Data de Fabricação é obrigatória")
	 * 
	 * @DateTimeFormat(pattern = "dd/MM/yyyy")
	 */
	@Temporal(TemporalType.DATE)
	private Date dataDeFabricacao;
	
	/*
	 * @NotNull(message = "Data de Vencimento é obrigatória")
	 * 
	 * @DateTimeFormat(pattern = "dd/MM/yyyy")
	 */
	@Temporal(TemporalType.DATE)
	private Date dataDeVencimento;
	
	//@NotNull(message = "Valor de Custo é obrigatório")
	@DecimalMin(value = "0.01", message = "Valor não pode ser menor que 0,01")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorDeCusto;
	
	//@NotNull(message = "Valor de Venda é obrigatório")
	@DecimalMin(value = "0.01", message = "Valor não pode ser menor que 0,01")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorDeVenda;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Categoria categoria;
	
	//@NotNull(message = "Quantidade não pode ser 0")
	private Integer quantidade;
	
	@OneToMany(mappedBy = "produto")
	private List<ProdutoFornecedor> produtoFornecedorList;
	
	@OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
	private List<Lote> loteList;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public List<ProdutoFornecedor> getProdutoFornecedorList() {
		return produtoFornecedorList;
	}

	public void setProdutoFornecedorList(List<ProdutoFornecedor> produtoFornecedorList) {
		this.produtoFornecedorList = produtoFornecedorList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public List<Lote> getLoteList() {
		return loteList;
	}

	public void setLoteList(List<Lote> loteList) {
		this.loteList = loteList;
	}

}
