package com.kaue.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class Fornecedor {
	
	@Id
	@SequenceGenerator(name = "seq_fornecedor", sequenceName = "seq_fornecedor", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = "seq_fornecedor", strategy = GenerationType.SEQUENCE)
	private Long codigo;
	
	@NotEmpty(message = "Nome é obrigatório")
	@Size(max = 255, message = "Nome não pode exceder 255 caracteres")
	private String nome;
		
	@Column(length = 100)
	private String cnpj;
	
	@Column(length = 50)
	private String telefone;
	
	@OneToOne
	private Endereco endereco;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	

}
