package com.kaue.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.model.Categoria;
import com.kaue.model.Grupo;
import com.kaue.model.Usuario;

public interface GrupoDAO extends JpaRepository<Grupo, Long>{
	public List<Grupo> findByDescricaoContainingIgnoreCaseOrderByIdDesc(String descricao);
	
	/* public List<Grupo> findByUsuariosIn(Usuario usuario); */
}
