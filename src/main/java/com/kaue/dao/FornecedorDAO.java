package com.kaue.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.model.Fornecedor;

public interface FornecedorDAO extends JpaRepository<Fornecedor, Long>{
	public List<Fornecedor> findByNomeContainingIgnoreCaseOrderByCodigoDesc(String nome);
}
