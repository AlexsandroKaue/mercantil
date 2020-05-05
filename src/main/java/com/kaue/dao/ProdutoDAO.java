package com.kaue.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.model.Produto;

public interface ProdutoDAO extends JpaRepository<Produto, Long>{

}
