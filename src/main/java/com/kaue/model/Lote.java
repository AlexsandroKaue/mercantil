package com.kaue.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Audited
public class Lote {
	
	/* @EmbeddedId private ProdutoFornecedorCK key; */
	 
	@Id
	@SequenceGenerator(name = "seq_lote", sequenceName = "seq_lote", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = "seq_lote" , strategy = GenerationType.SEQUENCE)
	private Long id;
	
	/* @MapsId(value = "codigoProduto") */
	@ManyToOne
	private Produto produto;
	
	/* @MapsId(value = "codigoFornecedor") */
	@ManyToOne
	private Fornecedor fornecedor;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	/* @NotNull(message = "O campo Data de Fabricação é obrigatório") */
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date dataFabricacao;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	/* @NotNull(message = "O campo Data de Vencimento é obrigatório") */
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date dataVencimento;
	
	@JsonFormat(pattern = "#,##0.00")
	@NotNull(message = "O campo Valor de Custo é obrigatório")
	@DecimalMin(value = "0.01", message = "Valor não pode ser inferior a R$ 0,01")
	@DecimalMax(value = "9999999.99", message = "Valor não pode ser superior R$ 9.999.999,99")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorCusto;
	
	@NotNull(message = "O campo Quantidade é obrigatório")
	@DecimalMin(value = "1.0", message = "Quantidade não pode ser inferior a 1")
	private Integer quantidade;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Date getDataFabricacao() {
		return dataFabricacao;
	}

	public void setDataFabricacao(Date dataFabricacao) {
		this.dataFabricacao = dataFabricacao;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public BigDecimal getValorCusto() {
		return valorCusto;
	}

	public void setValorCusto(BigDecimal valorCusto) {
		this.valorCusto = valorCusto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
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
		Lote other = (Lote) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
