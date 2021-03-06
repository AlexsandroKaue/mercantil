package com.kaue.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import com.kaue.enumeration.OpcoesDesconto;
import com.kaue.enumeration.StatusVenda;
import com.kaue.enumeration.TipoVenda;

@Entity
@Audited
public class Venda {
	
	@Id
	@SequenceGenerator(name = "seq_venda", sequenceName = "seq_venda", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "seq_venda", strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@NotNull(message = "Data de Venda é obrigatório")
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataVenda;
	
	/* @NotNull(message = "Valor é obrigatório") */
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal subtotal;
	
	/* @NotNull(message = "Valor é obrigatório") */
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal total;
	
	@Enumerated(EnumType.ORDINAL)
	private OpcoesDesconto desconto;
	
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorDesconto;
	
	//@NotNull(message = "O campo saldo é obrigatório")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal saldo;
	
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal troco;
	
	@Enumerated(EnumType.STRING)
	private StatusVenda status;
	
	@OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Item> itemList;
	
	@ManyToOne
	private Cliente cliente;
	
	@ManyToOne
	private Usuario usuario;
	
	@ManyToOne
	private Caixa caixa;
	
	@ManyToOne
	private Caderneta caderneta;
	
	@Enumerated(EnumType.STRING)
	private TipoVenda tipoVenda;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
	}

	public StatusVenda getStatus() {
		return status;
	}

	public void setStatus(StatusVenda status) {
		this.status = status;
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
		Venda other = (Venda) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public List<Item> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public BigDecimal getTroco() {
		return troco;
	}

	public void setTroco(BigDecimal troco) {
		this.troco = troco;
	}

	public OpcoesDesconto getDesconto() {
		return desconto;
	}

	public void setDesconto(OpcoesDesconto desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public TipoVenda getTipoVenda() {
		return tipoVenda;
	}

	public void setTipoVenda(TipoVenda tipoVenda) {
		this.tipoVenda = tipoVenda;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Caderneta getCaderneta() {
		return caderneta;
	}

	public void setCaderneta(Caderneta caderneta) {
		this.caderneta = caderneta;
	}

}
