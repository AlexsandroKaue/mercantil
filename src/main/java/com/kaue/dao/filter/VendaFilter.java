package com.kaue.dao.filter;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.kaue.model.Venda;

public class VendaFilter extends GenericFilter {
	
	private Venda venda = new Venda();
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dataInicio;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dataFim;

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}


}
