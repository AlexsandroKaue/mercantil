package com.kaue.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import com.kaue.enumeration.StatusCaixa;

@Entity
@Audited
public class Caixa extends PersistentEntityImpl {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "seq_caixa", sequenceName = "seq_caixa", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = "seq_caixa" , strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@NotNull(message = "O campo Valor Inicial é obrigatório")
	@DecimalMin(value = "0.00", message = "Valor inicial não pode ser menor que zero")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorInicial;
	
	@DecimalMin(value = "0.00", message = "Valor de vendas não pode ser menor que zero")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorVendas;
	
	@DecimalMin(value = "0.00", message = "Valor de Vendas em Dinheiro não pode ser menor que zero")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorVendasEmDinheiro;
	
	@DecimalMin(value = "0.00", message = "Valor de Vendas no Cartão não pode ser menor que zero")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorVendasEmCartao;
	
	@DecimalMin(value = "0.00", message = "Valor de recebimentos não pode ser menor que zero")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorRecebimentos;
	
	@DecimalMin(value = "0.00", message = "Valor de reforço não pode ser menor que zero")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorReforco;
	
	@DecimalMin(value = "0.00", message = "Valor de sangria não pode ser menor que zero")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorSangria;
	
	@DecimalMin(value = "0.00", message = "Valor de saídas não pode ser menor que zero")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorSaidas;
	
	@DecimalMin(value = "0.00", message = "Valor de entradas não pode ser menor que zero")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorEntradas;
	
	@DecimalMin(value = "0.00", message = "Valor final não pode ser menor que zero")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorFinal;
	
	@DecimalMin(value = "0.00", message = "Valor da gaveta não pode ser menor que zero")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorGaveta;
	
	@NotNull(message = "O campo Nº do Caixa é obrigatório")
	private Integer numero;
	
	@NotNull(message = "O campo Data de Abertura é obrigatório")
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAbertura;

	//@NotNull(message = "O campo Data de Abertura é obrigatório")
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFechamento;
	
	@ManyToOne
	@NotNull(message = "O campo Usuario de Abertura do Caixa é obrigatório")
	private Usuario usuarioAbertura;
	
	@ManyToOne
	private Usuario usuarioFechamento;
	
	@Enumerated(EnumType.STRING)
	private StatusCaixa status;
	
	/*
	 * @OneToMany(mappedBy = "caixa") private List<Venda> vendaList;
	 */
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(BigDecimal valorInicial) {
		this.valorInicial = valorInicial;
	}

	public BigDecimal getValorVendas() {
		return valorVendas;
	}

	public void setValorVendas(BigDecimal valorVendas) {
		this.valorVendas = valorVendas;
	}

	public BigDecimal getValorRecebimentos() {
		return valorRecebimentos;
	}

	public void setValorRecebimentos(BigDecimal valorRecebimentos) {
		this.valorRecebimentos = valorRecebimentos;
	}

	public BigDecimal getValorReforco() {
		return valorReforco;
	}

	public void setValorReforco(BigDecimal valorReforco) {
		this.valorReforco = valorReforco;
	}

	public BigDecimal getValorSangria() {
		return valorSangria;
	}

	public void setValorSangria(BigDecimal valorSangria) {
		this.valorSangria = valorSangria;
	}

	public BigDecimal getValorSaidas() {
		return valorSaidas;
	}

	public void setValorSaidas(BigDecimal valorSaidas) {
		this.valorSaidas = valorSaidas;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Date getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public Usuario getUsuarioAbertura() {
		return usuarioAbertura;
	}

	public void setUsuarioAbertura(Usuario usuarioAbertura) {
		this.usuarioAbertura = usuarioAbertura;
	}

	public Usuario getUsuarioFechamento() {
		return usuarioFechamento;
	}

	public void setUsuarioFechamento(Usuario usuarioFechamento) {
		this.usuarioFechamento = usuarioFechamento;
	}

	public StatusCaixa getStatus() {
		return status;
	}

	public void setStatus(StatusCaixa status) {
		this.status = status;
	}

	public BigDecimal getValorVendasEmDinheiro() {
		return valorVendasEmDinheiro;
	}

	public void setValorVendasEmDinheiro(BigDecimal valorVendasEmDinheiro) {
		this.valorVendasEmDinheiro = valorVendasEmDinheiro;
	}

	public BigDecimal getValorVendasEmCartao() {
		return valorVendasEmCartao;
	}

	public void setValorVendasEmCartao(BigDecimal valorVendasEmCartao) {
		this.valorVendasEmCartao = valorVendasEmCartao;
	}

	public BigDecimal getValorEntradas() {
		return valorEntradas;
	}

	public void setValorEntradas(BigDecimal valorEntradas) {
		this.valorEntradas = valorEntradas;
	}

	public BigDecimal getValorFinal() {
		return valorFinal;
	}

	public void setValorFinal(BigDecimal valorFinal) {
		this.valorFinal = valorFinal;
	}

	public BigDecimal getValorGaveta() {
		return valorGaveta;
	}

	public void setValorGaveta(BigDecimal valorGaveta) {
		this.valorGaveta = valorGaveta;
	}

	/*
	 * public List<Venda> getVendaList() { return vendaList; }
	 * 
	 * public void setVendaList(List<Venda> vendaList) { this.vendaList = vendaList;
	 * }
	 */
}
