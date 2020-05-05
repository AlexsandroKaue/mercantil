package com.kaue.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.model.Endereco;

public interface EnderecoDAO extends JpaRepository<Endereco, Long>{
	
}
