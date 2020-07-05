package com.kaue.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.dao.custom.ClienteRepositoryCustom;
import com.kaue.model.Cliente;

public interface ClienteDAO extends JpaRepository<Cliente, Long>, ClienteRepositoryCustom{

	public List<Cliente> findByNomeContainingIgnoreCaseOrderByIdDesc(String nome);
		
}
