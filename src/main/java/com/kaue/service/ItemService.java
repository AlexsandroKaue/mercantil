package com.kaue.service;

import java.util.List;

import com.kaue.model.Item;

public interface ItemService {
	
	public List<Item> pesquisarPorProduto(Long produtoId);

}
