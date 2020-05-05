package com.kaue.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.model.Fornecedor;

public interface FornecedorDAO extends JpaRepository<Fornecedor, Long>{
	
}
