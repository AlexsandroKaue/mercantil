package com.kaue.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotEmpty;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class Caderneta extends PersistentEntityImpl {
	
	private static final long serialVersionUID = -799851282301723693L;

	@Id
	@SequenceGenerator(name = "seq_caderneta", sequenceName = "seq_caderneta", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = "seq_caderneta" , strategy = GenerationType.SEQUENCE)
	private Long id;
	
	/*@OneToOne
	@NotEmpty(message = "O campo cliente é obrigatório")
	private Cliente cliente;*/
	
	private Boolean aberta;
	
	@OneToMany(mappedBy = "caderneta")
	private List<Venda> vendaList;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}*/

	public Boolean getAberta() {
		return aberta;
	}

	public void setAberta(Boolean aberta) {
		this.aberta = aberta;
	}

	public List<Venda> getVendaList() {
		return vendaList;
	}

	public void setVendaList(List<Venda> vendaList) {
		this.vendaList = vendaList;
	}

}
