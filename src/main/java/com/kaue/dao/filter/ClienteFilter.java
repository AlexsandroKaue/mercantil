package com.kaue.dao.filter;

import java.lang.reflect.Field;

import com.kaue.model.Cliente;

public class ClienteFilter extends GenericFilter {
	
	private Cliente cliente = new Cliente();
	
	private Field ordererField;
		
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

}
