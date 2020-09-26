package com.kaue.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.dao.custom.ClienteRepositoryCustom;
import com.kaue.dao.custom.FornecedorRepositoryCustom;
import com.kaue.model.Fornecedor;

public interface FornecedorDAO extends JpaRepository<Fornecedor, Long>, FornecedorRepositoryCustom{
	public List<Fornecedor> findByNomeContainingIgnoreCaseOrderByIdDesc(String nome);
}
