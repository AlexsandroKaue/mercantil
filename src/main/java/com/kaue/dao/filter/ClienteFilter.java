package com.kaue.dao.filter;

import java.lang.reflect.Field;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.kaue.model.Cliente;

public class ClienteFilter extends GenericFilter {
	
	private Cliente cliente = new Cliente();
	
	private Field ordererField;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dataInicio;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dataFim;
		
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Field getOrdererField() {
		return ordererField;
	}

	public void setOrdererField(Field ordererField) {
		this.ordererField = ordererField;
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
