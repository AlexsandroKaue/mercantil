package com.kaue.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.dao.custom.CadernetaRepositoryCustom;
import com.kaue.model.Caderneta;

public interface CadernetaDAO extends JpaRepository<Caderneta, Long>, CadernetaRepositoryCustom{
	//public List<Caderneta> findByDescricaoContainingIgnoreCaseOrderByIdDesc(String descricao);
}
