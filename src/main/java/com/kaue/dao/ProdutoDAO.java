package com.kaue.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.model.Produto;

public interface ProdutoDAO extends JpaRepository<Produto, Long>{

	public List<Produto> findByDescricaoContainingIgnoreCaseOrderByIdDesc(String descricao);
	
	public Produto findByCodigo(String codigo);

}
