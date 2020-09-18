package com.kaue.dao.filter;

import com.kaue.model.Cliente;

public class ClienteFilter extends GenericFilter {
	
	private Cliente cliente = new Cliente();
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
