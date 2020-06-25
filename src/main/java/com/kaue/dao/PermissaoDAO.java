package com.kaue.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.model.Grupo;
import com.kaue.model.Permissao;

public interface PermissaoDAO extends JpaRepository<Permissao, Long>{
	
	public List<Permissao> findByDescricaoContainingIgnoreCase(String descricao);
	
	/* public List<Permissao> findByGruposIn(Grupo grupo); */

}
