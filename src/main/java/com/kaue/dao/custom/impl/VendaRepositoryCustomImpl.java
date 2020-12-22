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

import com.kaue.dao.custom.VendaRepositoryCustom;
import com.kaue.dao.filter.VendaFilter;
import com.kaue.model.Venda;

public class VendaRepositoryCustomImpl implements VendaRepositoryCustom{

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public List<Venda> findVendaByFiltro(VendaFilter vendaFiltro) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Venda> query = cb.createQuery(Venda.class);
        Root<Venda> venda = query.from(Venda.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(vendaFiltro, cb, venda);
        
        if(predicates.size()>0) {
        	if(vendaFiltro.isAvancada()) {
        		query.select(venda).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        	} else {
        		query.select(venda).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        	}
        } else {
        	query.select(venda);
        }
        query.orderBy(cb.desc(venda.get("id")));
        
        TypedQuery<Venda> typedQuery = null;
        boolean isPaginated = vendaFiltro.isPaginated();
        if(isPaginated) {
        	Long page = vendaFiltro.getPage();
        	Long pageSize = vendaFiltro.getPageSize();
        	int start = page.intValue()*pageSize.intValue();
        	typedQuery = entityManager.createQuery(query).setFirstResult(start).setMaxResults(pageSize.intValue());
        } else {
        	typedQuery = entityManager.createQuery(query);
        }
 
        return typedQuery.getResultList();
	}

	@Override
	public Long countVendaByFiltro(VendaFilter vendaFiltro) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Venda> venda = query.from(Venda.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(vendaFiltro, cb, venda);
        
        if(predicates.size()>0) {
        	if(vendaFiltro.isAvancada()) {
        		query.select(cb.count(venda)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        	} else {
        		query.select(cb.count(venda)).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        	}
        	
        } else {
        	query.select(cb.count(venda));
        }
        
        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
 
        return typedQuery.getSingleResult();
	}
	
	private List<Predicate> prepararPredicadosDaConsulta(VendaFilter vendaFiltro, 
			CriteriaBuilder cb, 
			Root<Venda> venda) {
		
		List<Predicate> predicates = new ArrayList<>();
		Venda vendaConsulta = vendaFiltro.getVenda();
        
        Path<Long> idPath = null;
        Path<String> dataPath = null;
        Path<String> descricaoPath = null;
        
        
        if(vendaConsulta.getId()!=null) {
        	idPath = venda.get("id");
        	predicates.add(cb.equal(idPath, vendaConsulta.getId()));
        }
        
        return predicates;
	}

}
