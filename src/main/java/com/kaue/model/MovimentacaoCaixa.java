package com.kaue.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.NumberFormat;

import com.kaue.enumeration.TipoMovimentacaoCaixa;

@Entity
@Audited
public class MovimentacaoCaixa extends PersistentEntityImpl {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "seq_mov_caixa", sequenceName = "seq_mov_caixa", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = "seq_mov_caixa" , strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private TipoMovimentacaoCaixa tipoMovimentacaoCaixa;
	
	//@NotNull(message = "O campo Valor é obrigatório")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valor;
	
	@NotBlank(message = "O campo Motivo é obrigatório")
	@Size(max = 255, message = "O motivo não pode conter mais de 255 caracteres")
	private String motivo;
	
	@ManyToOne
	private Caixa caixa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoMovimentacaoCaixa getTipoMovimentacaoCaixa() {
		return tipoMovimentacaoCaixa;
	}

	public void setTipoMovimentacaoCaixa(TipoMovimentacaoCaixa tipoMovimentacaoCaixa) {
		this.tipoMovimentacaoCaixa = tipoMovimentacaoCaixa;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}
}
