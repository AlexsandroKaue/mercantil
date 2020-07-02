package com.kaue.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.dao.custom.GrupoRepositoryCustom;
import com.kaue.model.Grupo;
import com.kaue.model.Usuario;

public interface GrupoDAO extends JpaRepository<Grupo, Long>, GrupoRepositoryCustom{
	
	public List<Usuario> findByNomeOrDescricaoContainingIgnoreCaseOrderByIdDesc(String nome, String descricao);
	
	/* public List<Grupo> findByUsuariosIn(Usuario usuario); */
}
