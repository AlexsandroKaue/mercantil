package com.kaue.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotEmpty;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class Categoria extends PersistentEntityImpl {
	
	private static final long serialVersionUID = 6294317934527581227L;

	@Id
	@SequenceGenerator(name = "seq_categoria", sequenceName = "seq_categoria", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = "seq_categoria" , strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@NotEmpty(message = "Nome é um campo obrigatório")
	@Column(length = 100)
	private String nome;
	
	@Column(length = 100)
	private String descricao;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/*
	 * @Override public int hashCode() { final int prime = 31; int result = 1;
	 * result = prime * result + ((id == null) ? 0 : id.hashCode()); return result;
	 * }
	 */

	/*
	 * @Override public boolean equals(Object obj) { if (this == obj) return true;
	 * if (obj == null) return false; if (getClass() != obj.getClass()) return
	 * false; Categoria other = (Categoria) obj; if (id == null) { if (other.id !=
	 * null) return false; } else if (!id.equals(other.id)) return false; return
	 * true; }
	 */

}
