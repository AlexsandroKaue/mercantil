package com.kaue.dao.custom.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.kaue.dao.custom.CategoriaRepositoryCustom;
import com.kaue.dao.filter.CategoriaFilter;
import com.kaue.model.Categoria;
import com.kaue.model.Grupo;

public class CategoriaRepositoryCustomImpl implements CategoriaRepositoryCustom{

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public List<Categoria> findCategoriaByFiltro(CategoriaFilter categoriaFiltro) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Categoria> query = cb.createQuery(Categoria.class);
        Root<Categoria> categoria = query.from(Categoria.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(categoriaFiltro, cb, categoria);
        
        if(predicates.size()>0) {
        	if(categoriaFiltro.isAvancada()) {
        		query.select(categoria).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        	} else {
        		query.select(categoria).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        	}
        } else {
        	query.select(categoria);
        }
        query.orderBy(cb.desc(categoria.get("id")));
        
        TypedQuery<Categoria> typedQuery = null;
        boolean isPaginated = categoriaFiltro.isPaginated();
        if(isPaginated) {
        	Long page = categoriaFiltro.getPage();
        	Long pageSize = categoriaFiltro.getPageSize();
        	int start = page.intValue()*pageSize.intValue();
        	typedQuery = entityManager.createQuery(query).setFirstResult(start).setMaxResults(pageSize.intValue());
        } else {
        	typedQuery = entityManager.createQuery(query);
        }
 
        return typedQuery.getResultList();
	}

	@Override
	public Long countCategoriaByFiltro(CategoriaFilter categoriaFiltro) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Categoria> categoria = query.from(Categoria.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(categoriaFiltro, cb, categoria);
        
        if(predicates.size()>0) {
        	if(categoriaFiltro.isAvancada()) {
        		query.select(cb.count(categoria)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        	} else {
        		query.select(cb.count(categoria)).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        	}
        	
        } else {
        	query.select(cb.count(categoria));
        }
        
        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
 
        return typedQuery.getSingleResult();
	}
	
	private List<Predicate> prepararPredicadosDaConsulta(CategoriaFilter categoriaFiltro, 
			CriteriaBuilder cb, 
			Root<Categoria> categoria) {
		
		List<Predicate> predicates = new ArrayList<>();
		Categoria categoriaConsulta = categoriaFiltro.getCategoria();
        
        Path<Long> idPath = null;
        Path<String> nomePath = null;
        Path<String> descricaoPath = null;
        
        
        if(categoriaConsulta.getId()!=null) {
        	idPath = categoria.get("id");
        	predicates.add(cb.equal(idPath, categoriaConsulta.getId()));
        }
        if(categoriaConsulta.getNome()!=null) {
        	nomePath = categoria.get("nome");
        	predicates.add(cb.like(cb.upper(nomePath), "%"+categoriaConsulta.getNome().toUpperCase()+"%"));
        }
        if(categoriaConsulta.getDescricao()!=null) {
        	descricaoPath = categoria.get("descricao");
        	predicates.add(cb.like(cb.upper(descricaoPath), 
        			"%"+categoriaConsulta.getDescricao().toUpperCase()+"%"));
        }
        
        return predicates;
	}

}
