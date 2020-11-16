package com.kaue.service;

import com.frontbackend.thymeleaf.bootstrap.model.paging.Page;
import com.frontbackend.thymeleaf.bootstrap.model.paging.PagingRequest;
import com.kaue.model.Produto;

public interface ProdutoDTService {
	public Page<Produto> getProdutos(PagingRequest pagingRequest);
}
