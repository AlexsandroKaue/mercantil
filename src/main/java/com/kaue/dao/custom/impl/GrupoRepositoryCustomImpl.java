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

import com.kaue.dao.custom.GrupoRepositoryCustom;
import com.kaue.dao.filter.GrupoFilter;
import com.kaue.model.Grupo;

public class GrupoRepositoryCustomImpl implements GrupoRepositoryCustom{

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public List<Grupo> findGrupoByFiltro(GrupoFilter grupoFiltro) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Grupo> query = cb.createQuery(Grupo.class);
        Root<Grupo> grupo = query.from(Grupo.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(grupoFiltro, cb, grupo);
        
        if(predicates.size()>0) {
        	if(grupoFiltro.isAvancada()) {
        		query.select(grupo).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        	} else {
        		query.select(grupo).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        	}
        } else {
        	query.select(grupo);
        }
        query.orderBy(cb.desc(grupo.get("id")));
        
        TypedQuery<Grupo> typedQuery = null;
        boolean isPaginated = grupoFiltro.isPaginated();
        if(isPaginated) {
        	Long page = grupoFiltro.getPage();
        	Long pageSize = grupoFiltro.getPageSize();
        	int start = page.intValue()*pageSize.intValue();
        	typedQuery = entityManager.createQuery(query).setFirstResult(start).setMaxResults(pageSize.intValue());
        } else {
        	typedQuery = entityManager.createQuery(query);
        }
 
        return typedQuery.getResultList();
	}

	@Override
	public Long countGrupoByFiltro(GrupoFilter grupoFiltro) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Grupo> grupo = query.from(Grupo.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(grupoFiltro, cb, grupo);
        
        if(predicates.size()>0) {
        	if(grupoFiltro.isAvancada()) {
        		query.select(cb.count(grupo)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        	} else {
        		query.select(cb.count(grupo)).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        	}
        } else {
        	query.select(cb.count(grupo));
        }
        
        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
 
        return typedQuery.getSingleResult();
	}
	
	private List<Predicate> prepararPredicadosDaConsulta(GrupoFilter grupoFiltro, 
			CriteriaBuilder cb, 
			Root<Grupo> grupo) {
		
		List<Predicate> predicates = new ArrayList<>();
		Grupo grupoConsulta = grupoFiltro.getGrupo();
        
        Path<Long> idPath = null;
        Path<String> nomePath = null;
        Path<String> descricaoPath = null;
        
        if(grupoConsulta.getId()!=null) {
        	idPath = grupo.get("id");
        	predicates.add(cb.equal(idPath, grupoConsulta.getId()));
        }
        if(grupoConsulta.getNome()!=null) {
        	nomePath = grupo.get("nome");
        	predicates.add(cb.like(cb.upper(nomePath), "%"+grupoConsulta.getNome().toUpperCase()+"%"));
        }
        if(grupoConsulta.getDescricao()!=null) {
        	descricaoPath = grupo.get("descricao");
        	predicates.add(cb.like(cb.upper(descricaoPath), 
        			"%"+grupoConsulta.getDescricao().toUpperCase()+"%"));
        }
        
        return predicates;
	}

}
