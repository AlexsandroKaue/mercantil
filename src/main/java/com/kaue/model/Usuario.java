package com.kaue.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

import com.kaue.enumeration.StatusUsuario;

@Entity
@Audited
public class Usuario {
	
	@Id
	@SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "seq_usuario", strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@NotEmpty(message = "O campo nome é obrigatório")
	@Size(max = 100, message = "O campo nome não pode conter mais de 100 caracteres")
	private String nome;
	
	@Size(max = 100, message = "O campo email não pode conter mais de 100 caracteres")
	private String email;
	
	@NotEmpty(message = "O campo login é obrigatório")
	@Size(max = 100, message = "O  campo login não pode conter mais de 100 caracteres")
	private String login;
	
	@NotEmpty(message = "O campo senha é obrigatório")
	private String senha;
	
	private String foto;
	
	@Enumerated(EnumType.STRING)
	private StatusUsuario ativo;
	
	@Lob
	private byte[] imagem;
	
	@ManyToOne
	private Grupo grupo;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public StatusUsuario getAtivo() {
		return ativo;
	}

	public void setAtivo(StatusUsuario ativo) {
		this.ativo = ativo;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}
	
	

}
