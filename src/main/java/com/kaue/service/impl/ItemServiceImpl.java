package com.kaue.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaue.dao.CategoriaDAO;
import com.kaue.dao.FornecedorDAO;
import com.kaue.dao.ItemDAO;
import com.kaue.dao.filter.CategoriaFilter;
import com.kaue.dao.filter.FornecedorFilter;
import com.kaue.model.Categoria;
import com.kaue.model.Fornecedor;
import com.kaue.model.Item;
import com.kaue.model.Usuario;
import com.kaue.service.CategoriaService;
import com.kaue.service.FornecedorService;
import com.kaue.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService{
	
	@Autowired
	private ItemDAO itemDAO;

	@Override
	public List<Item> pesquisarPorProduto(Long produtoId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
