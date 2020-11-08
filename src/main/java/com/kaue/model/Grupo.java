package com.kaue.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class Grupo {
	
	@Id
	@SequenceGenerator(name = "seq_grupo", sequenceName = "seq_grupo", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "seq_grupo", strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@NotEmpty(message = "O campo <strong>nome</strong> é obrigatório")
	private String nome;
	
	@NotEmpty(message = "O campo <strong>descrição</strong> é obrigatório")
	private String descricao;
	
	@OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<GrupoPermissao> grupoPermissaoList;
	
	@Transient
	private List<Permissao> permissaoList;

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

	public List<GrupoPermissao> getGrupoPermissaoList() {
		return grupoPermissaoList;
	}

	public void setGrupoPermissaoList(List<GrupoPermissao> grupoPermissaoList) {
		this.grupoPermissaoList = grupoPermissaoList;
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
		Grupo other = (Grupo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Grupo [id=" + id + ", nome=" + nome + "]";
	}

	public List<Permissao> getPermissaoList() {
		return permissaoList;
	}

	public void setPermissaoList(List<Permissao> permissaoList) {
		this.permissaoList = permissaoList;
	}

}
