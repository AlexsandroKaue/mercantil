package com.kaue.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.dao.custom.UsuarioRepositoryCustom;
import com.kaue.model.Usuario;

public interface UsuarioDAO extends JpaRepository<Usuario, Long>, UsuarioRepositoryCustom{

	public List<Usuario> findByNomeContainingIgnoreCaseOrderByIdDesc(String nome);
	
	public Usuario findByLogin(String login);
}
