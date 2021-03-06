package com.kaue.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.dao.custom.CategoriaRepositoryCustom;
import com.kaue.model.Categoria;
import com.kaue.model.Fornecedor;

public interface CategoriaDAO extends JpaRepository<Categoria, Long>, CategoriaRepositoryCustom{
	public List<Categoria> findByDescricaoContainingIgnoreCaseOrderByIdDesc(String descricao);
}
