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

import com.kaue.dao.custom.CadernetaRepositoryCustom;
import com.kaue.dao.filter.CadernetaFilter;
import com.kaue.model.Caderneta;

public class CadernetaRepositoryCustomImpl implements CadernetaRepositoryCustom{

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public List<Caderneta> findCadernetaByFiltro(CadernetaFilter cadernetaFiltro) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Caderneta> query = cb.createQuery(Caderneta.class);
        Root<Caderneta> caderneta = query.from(Caderneta.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(cadernetaFiltro, cb, caderneta);
        
        if(predicates.size()>0) {
        	if(cadernetaFiltro.isAvancada()) {
        		query.select(caderneta).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        	} else {
        		query.select(caderneta).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        	}
        } else {
        	query.select(caderneta);
        }
        query.orderBy(cb.desc(caderneta.get("id")));
        
        TypedQuery<Caderneta> typedQuery = null;
        boolean isPaginated = cadernetaFiltro.isPaginated();
        if(isPaginated) {
        	Long page = cadernetaFiltro.getPage();
        	Long pageSize = cadernetaFiltro.getPageSize();
        	int start = page.intValue()*pageSize.intValue();
        	typedQuery = entityManager.createQuery(query).setFirstResult(start).setMaxResults(pageSize.intValue());
        } else {
        	typedQuery = entityManager.createQuery(query);
        }
 
        return typedQuery.getResultList();
	}

	@Override
	public Long countCadernetaByFiltro(CadernetaFilter cadernetaFiltro) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Caderneta> caderneta = query.from(Caderneta.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(cadernetaFiltro, cb, caderneta);
        
        if(predicates.size()>0) {
        	if(cadernetaFiltro.isAvancada()) {
        		query.select(cb.count(caderneta)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        	} else {
        		query.select(cb.count(caderneta)).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        	}
        	
        } else {
        	query.select(cb.count(caderneta));
        }
        
        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
 
        return typedQuery.getSingleResult();
	}
	
	private List<Predicate> prepararPredicadosDaConsulta(CadernetaFilter cadernetaFiltro, 
			CriteriaBuilder cb, 
			Root<Caderneta> caderneta) {
		
		List<Predicate> predicates = new ArrayList<>();
		Caderneta cadernetaConsulta = cadernetaFiltro.getCaderneta();
        
        Path<Long> idPath = null;
        Path<String> nomePath = null;
        Path<String> descricaoPath = null;
        
        
        if(cadernetaConsulta.getId()!=null) {
        	idPath = caderneta.get("id");
        	predicates.add(cb.equal(idPath, cadernetaConsulta.getId()));
        }
        
        return predicates;
	}

}
