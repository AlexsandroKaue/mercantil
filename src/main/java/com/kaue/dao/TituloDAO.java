package com.kaue.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.model.Titulo;

public interface TituloDAO extends JpaRepository<Titulo, Long>{

	public List<Titulo> findByDescricaoContainingIgnoreCase(String descricao);
}
