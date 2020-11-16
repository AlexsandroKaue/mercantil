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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kaue.dao.custom.ProdutoRepositoryCustom;
import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.enumeration.Unitario;
import com.kaue.model.Produto;
import com.kaue.model.Usuario;
import com.kaue.util.HasValue;

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
        	if(produtoFiltro.isAvancada()) {
        		query.select(produto).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        	} else {
        		query.select(produto).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        	}
        } else {
        	query.select(produto);
        }
        query.orderBy(cb.desc(produto.get("id")));
        
        TypedQuery<Produto> typedQuery = null;
        Long page = produtoFiltro.getPage();
        Long pageSize = produtoFiltro.getPageSize();
        if(page!=null) {
        	Long start = HasValue.execute(produtoFiltro.getStart())?produtoFiltro.getStart():new Long(page.intValue()*pageSize.intValue());
        	typedQuery = entityManager.createQuery(query).setFirstResult(start.intValue()).setMaxResults(pageSize.intValue());
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
        	if(produtoFiltro.isAvancada()) {
        		query.select(cb.count(produto)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        	} else {
        		query.select(cb.count(produto)).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        	}
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
        Path<String> marcaPath = null;
        Path<Integer> quantidadePath = null;
        Path<Unitario> unitarioPath = null;
        
        if(produtoFiltro.getProduto().getCodigo()!=null) {
        	codigoPath = produto.get("codigo");
        	predicates.add(cb.like(codigoPath, "%"+produtoFiltro.getProduto().getCodigo()+"%"));
        }
        if(produtoFiltro.getProduto().getDescricao()!=null) {
        	descricaoPath = produto.get("descricao");
        	predicates.add(cb.like(cb.upper(descricaoPath), "%"+produtoFiltro.getProduto().getDescricao().toUpperCase()+"%"));
        }
        if(produtoFiltro.getProduto().getCategoria()!=null) {
        	categoriaPath = produto.get("categoria").get("descricao");
        	predicates.add(cb.like(cb.upper(categoriaPath), 
        			"%"+produtoFiltro.getProduto().getCategoria().getDescricao().toUpperCase()+"%"));
        }
        if(produtoFiltro.getProduto().getMarca()!=null) {
        	marcaPath = produto.get("marca");
        	predicates.add(cb.like(cb.upper(marcaPath), "%"+produtoFiltro.getProduto().getMarca().toUpperCase()+"%"));
        }
        if(produtoFiltro.getProduto().getQuantidade()!=null) {
        	quantidadePath = produto.get("quantidade");
        	predicates.add(cb.le(quantidadePath, produtoFiltro.getProduto().getQuantidade()));
        }
        if(produtoFiltro.getProduto().getUnitario()!=null) {
        	unitarioPath = produto.get("unitario");
        	predicates.add(cb.equal(unitarioPath, produtoFiltro.getProduto().getUnitario()));
        }
        
        return predicates;
	}
	
	@Override
	public Long obterMaxId() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Produto> produto = query.from(Produto.class);
        
        query.select(cb.max(produto.get("id")));
        
        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
		
		return typedQuery.getSingleResult();
	}

	@Override
	public Page<Produto> findProdutoByFiltro(ProdutoFilter produtoFiltro, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

}
