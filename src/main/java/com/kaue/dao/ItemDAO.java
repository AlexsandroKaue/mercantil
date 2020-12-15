package com.kaue.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.dao.custom.CategoriaRepositoryCustom;
import com.kaue.model.Categoria;
import com.kaue.model.Fornecedor;
import com.kaue.model.Item;

public interface ItemDAO extends JpaRepository<Item, Long> {
	public List<Item> findByProduto(Long produtoId);
}
