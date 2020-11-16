package com.kaue.service.impl;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Column;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Order;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Page;
import com.frontbackend.thymeleaf.bootstrap.model.paging.PagingRequest;
import com.frontbackend.thymeleaf.bootstrap.model.paging.ProdutoComparators;
import com.kaue.dao.ProdutoDAO;
import com.kaue.model.Produto;
import com.kaue.service.ProdutoDTService;
import com.kaue.service.ProdutoService;

@Service
public class ProdutoDTServiceImpl implements ProdutoDTService{
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ProdutoDAO produtoDAO;
	
	private static final Comparator<Produto> EMPTY_COMPARATOR = (e1, e2) -> 0;

    public Page<Produto> getProdutos(PagingRequest pagingRequest) {
        ObjectMapper objectMapper = new ObjectMapper();

		/* try { */
        	//List<Produto> produtos = produtoDAO.findAll();
        	List<Produto> produtos = produtoService.pesquisar(pagingRequest.getFiltro());
            /*List<Produto> produtos = objectMapper.readValue(getClass().getClassLoader()
                                                                        .getResourceAsStream("produtos.json"),
                    new TypeReference<List<Produto>>() {
                    });*/
            return getPage(produtos, pagingRequest);

        /*} catch (IOException e) {
        	e.printStackTrace();
            //log.error(e.getMessage(), e);
        }*/

        //return new Page<>();
        //return null;
    }

    private Page<Produto> getPage(List<Produto> produtos, PagingRequest pagingRequest) {
        List<Produto> filtered = produtos.stream()
                                           .sorted(sortProdutos(pagingRequest))
                                           .filter(filterProdutos(pagingRequest))
                                           .skip(pagingRequest.getStart())
                                           .limit(pagingRequest.getLength())
                                           .collect(Collectors.toList());
        filtered = produtos;

        long count = produtos.stream()
                             .filter(filterProdutos(pagingRequest))
                             .count();
        count = produtoService.contar(pagingRequest.getFiltro());

        Page<Produto> page = new Page<>(filtered);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());

        return page;
    }

    private Predicate<Produto> filterProdutos(PagingRequest pagingRequest) {
        if (pagingRequest.getSearch() == null || StringUtils.isEmpty(pagingRequest.getSearch()
                                                                                  .getValue())) {
            return produto -> true;
        }

        String value = pagingRequest.getSearch()
                                    .getValue();

        return produto -> produto.getDescricao()
                                   .toLowerCase()
                                   .contains(value)
                || produto.getMarca()
                           .toLowerCase()
                           .contains(value)
                || produto.getCategoria().getDescricao()
                           .toLowerCase()
                           .contains(value);
    }

    private Comparator<Produto> sortProdutos(PagingRequest pagingRequest) {
        if (pagingRequest.getOrder() == null) {
            return EMPTY_COMPARATOR;
        }

        try {
            Order order = pagingRequest.getOrder()
                                       .get(0);

            int columnIndex = order.getColumn();
            Column column = pagingRequest.getColumns()
                                         .get(columnIndex);

            Comparator<Produto> comparator = ProdutoComparators.getComparator(column.getData(), order.getDir());
            if (comparator == null) {
                return EMPTY_COMPARATOR;
            }

            return comparator;

        } catch (Exception e) {
        	e.printStackTrace();
            //log.error(e.getMessage(), e);
        }

        return EMPTY_COMPARATOR;
    }
	
}
