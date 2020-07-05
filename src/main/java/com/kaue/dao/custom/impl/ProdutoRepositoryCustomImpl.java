package com.kaue.dao.custom.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.kaue.dao.custom.ProdutoRepositoryCustom;
import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.model.Produto;

public class ProdutoRepositoryCustomImpl implements ProdutoRepositoryCustom{

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public List<Produto> findProdutoByFiltro(ProdutoFilter produtoFiltro) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> query = cb.createQuery(Produto.class);
        Root<Produto> produto = query.from(Produto.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(produtoFiltro, cb, produto);
        
        if(predicates.size()>0) {
        	query.select(produto).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        } else {
        	query.select(produto);
        }
        query.orderBy(cb.desc(produto.get("id")));
        
        TypedQuery<Produto> typedQuery = null;
        Long page = produtoFiltro.getPage();
        Long pageSize = produtoFiltro.getPageSize();
        if(page!=null) {
        	int start = page.intValue()*pageSize.intValue();
        	typedQuery = entityManager.createQuery(query).setFirstResult(start).setMaxResults(pageSize.intValue());
        } else {
        	typedQuery = entityManager.createQuery(query);
        }
 
        return typedQuery.getResultList();
	}

	@Override
	public Long countProdutoByFiltro(ProdutoFilter produtoFiltro) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Produto> produto = query.from(Produto.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(produtoFiltro, cb, produto);
        
        if(predicates.size()>0) {
        	query.select(cb.count(produto)).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        } else {
        	query.select(cb.count(produto));
        }
        
        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
 
        return typedQuery.getSingleResult();
	}
	
	private List<Predicate> prepararPredicadosDaConsulta(ProdutoFilter produtoFiltro, 
			CriteriaBuilder cb, 
			Root<Produto> produto) {
		
		List<Predicate> predicates = new ArrayList<>();
        
        Path<String> codigoPath = null;
        Path<String> descricaoPath = null;
        Path<String> categoriaPath = null;
        if(produtoFiltro.getCodigo()!=null) {
        	codigoPath = produto.get("codigo");
        	predicates.add(cb.like(codigoPath, "%"+produtoFiltro.getCodigo()+"%"));
        }
        if(produtoFiltro.getDescricao()!=null) {
        	descricaoPath = produto.get("descricao");
        	predicates.add(cb.like(cb.upper(descricaoPath), "%"+produtoFiltro.getDescricao().toUpperCase()+"%"));
        }
        if(produtoFiltro.getCategoria()!=null) {
        	categoriaPath = produto.get("categoria").get("descricao");
        	predicates.add(cb.like(cb.upper(categoriaPath), 
        			"%"+produtoFiltro.getCategoria().getDescricao().toUpperCase()+"%"));
        }
        
        return predicates;
	}

}
