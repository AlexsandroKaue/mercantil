package com.kaue.dao.custom.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.kaue.dao.custom.CaixaRepositoryCustom;
import com.kaue.dao.filter.CaixaFilter;
import com.kaue.model.Caixa;
import com.kaue.model.Usuario;
import com.kaue.util.HasValue;

public class CaixaRepositoryCustomImpl implements CaixaRepositoryCustom{

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public List<Caixa> findCaixaByFiltro(CaixaFilter caixaFiltro) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Caixa> query = cb.createQuery(Caixa.class);
        Root<Caixa> caixa = query.from(Caixa.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(caixaFiltro, cb, caixa);
        
        if(predicates.size()>0) {
        	if(caixaFiltro.isAvancada()) {
        		query.select(caixa).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        	} else {
        		query.select(caixa).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        	}
        } else {
        	query.select(caixa);
        }
        query.orderBy(cb.desc(caixa.get("id")));
        
        TypedQuery<Caixa> typedQuery = null;
        boolean isPaginated = caixaFiltro.isPaginated();
        if(isPaginated) {
        	Long page = caixaFiltro.getPage();
        	Long pageSize = caixaFiltro.getPageSize();
        	int start = page.intValue()*pageSize.intValue();
        	typedQuery = entityManager.createQuery(query).setFirstResult(start).setMaxResults(pageSize.intValue());
        } else {
        	typedQuery = entityManager.createQuery(query);
        }
 
        return typedQuery.getResultList();
	}

	@Override
	public Long countCaixaByFiltro(CaixaFilter caixaFiltro) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Caixa> caixa = query.from(Caixa.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(caixaFiltro, cb, caixa);
        
        if(predicates.size()>0) {
        	if(caixaFiltro.isAvancada()) {
        		query.select(cb.count(caixa)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        	} else {
        		query.select(cb.count(caixa)).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        	}
        	
        } else {
        	query.select(cb.count(caixa));
        }
        
        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
 
        return typedQuery.getSingleResult();
	}
	
	private List<Predicate> prepararPredicadosDaConsulta(CaixaFilter caixaFiltro, 
			CriteriaBuilder cb, 
			Root<Caixa> caixa) {
		
		List<Predicate> predicates = new ArrayList<>();
		Caixa caixaConsulta = caixaFiltro.getCaixa();
        
        Path<Long> idPath = null;
        Path<String> numeroPath = null;
        Path<Date> dataAberturaPath = null;
        Path<Date> dataFechamentoPath = null;
        Path<Usuario> usuarioAberturaPath = null;
        Path<Usuario> usuarioFechamentoPath = null;
        
        
        if(HasValue.execute(caixaConsulta.getId())) {
        	idPath = caixa.get("id");
        	predicates.add(cb.equal(idPath, caixaConsulta.getId()));
        }
        if(HasValue.execute(caixaConsulta.getNumero())) {
        	numeroPath = caixa.get("numero");
        	predicates.add(cb.like(numeroPath, "%"+caixaConsulta.getNumero()+"%"));
        }
        if(HasValue.execute(caixaConsulta.getDataAbertura())) {
        	dataAberturaPath = caixa.get("dataAbertura");
        	predicates.add(cb.greaterThanOrEqualTo(dataAberturaPath, caixaConsulta.getDataAbertura()));
        }
        if(HasValue.execute(caixaConsulta.getDataFechamento())) {
        	dataFechamentoPath = caixa.get("dataFechamento");
        	predicates.add(cb.lessThanOrEqualTo(dataFechamentoPath, caixaConsulta.getDataFechamento()));
        }
        if(HasValue.execute(caixaConsulta.getUsuarioAbertura())) {
        	usuarioAberturaPath = caixa.get("usuarioAbertura");
        	predicates.add(cb.equal(usuarioAberturaPath, caixaConsulta.getUsuarioAbertura()));
        }
        if(HasValue.execute(caixaConsulta.getUsuarioFechamento())) {
        	usuarioFechamentoPath = caixa.get("usuarioFechamento");
        	predicates.add(cb.equal(usuarioFechamentoPath, caixaConsulta.getUsuarioFechamento()));
        }
        
        return predicates;
	}

	@Override
	public Caixa obterCaixaMaisRecente() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Caixa> query = cb.createQuery(Caixa.class);
        Root<Caixa> caixa = query.from(Caixa.class);
        
        //Get max id da tabela Caixa
        Long maxId = obterMaxId();
        if(HasValue.execute(maxId)) {
	        
	        query.select(caixa).where(cb.equal(caixa.get("id"), maxId));
	        
	        TypedQuery<Caixa> typedQuery = entityManager.createQuery(query);
	        
	        return typedQuery.getSingleResult();
        } 
        return null;
	}
	
	private Long obterMaxId() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Caixa> caixa = query.from(Caixa.class);
        
        query.select(cb.max(caixa.get("id")));
        
        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
		
		return typedQuery.getSingleResult();
	}
}
