package com.kaue.dao.custom.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.kaue.dao.custom.FornecedorRepositoryCustom;
import com.kaue.dao.filter.FornecedorFilter;
import com.kaue.model.Fornecedor;

public class FornecedorRepositoryCustomImpl implements FornecedorRepositoryCustom {

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public List<Fornecedor> findFornecedorByFiltro(FornecedorFilter fornecedorFiltro) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Fornecedor> query = cb.createQuery(Fornecedor.class);
        Root<Fornecedor> fornecedor = query.from(Fornecedor.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(fornecedorFiltro, cb, fornecedor);
        
        if(predicates.size()>0) {
        	if(fornecedorFiltro.isAvancada()) {
        		query.select(fornecedor).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        	} else {
        		query.select(fornecedor).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        	}
        } else {
        	query.select(fornecedor);
        }
        query.orderBy(cb.desc(fornecedor.get("id")));
        
        TypedQuery<Fornecedor> typedQuery = null;
        boolean isPaginated = fornecedorFiltro.isPaginated();
        if(isPaginated) {
        	Long page = fornecedorFiltro.getPage();
        	Long pageSize = fornecedorFiltro.getPageSize();
        	int start = page.intValue()*pageSize.intValue();
        	typedQuery = entityManager.createQuery(query).setFirstResult(start).setMaxResults(pageSize.intValue());
        } else {
        	typedQuery = entityManager.createQuery(query);
        }
 
        return typedQuery.getResultList();
	}

	@Override
	public Long countFornecedorByFiltro(FornecedorFilter fornecedorFiltro) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Fornecedor> fornecedor = query.from(Fornecedor.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(fornecedorFiltro, cb, fornecedor);
        
        if(predicates.size()>0) {
        	if(fornecedorFiltro.isAvancada()) {
        		query.select(cb.count(fornecedor)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        	} else {
        		query.select(cb.count(fornecedor)).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        	}
        } else {
        	query.select(cb.count(fornecedor));
        }
        
        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
 
        return typedQuery.getSingleResult();
	}
	
	private List<Predicate> prepararPredicadosDaConsulta(FornecedorFilter fornecedorFiltro, 
			CriteriaBuilder cb, 
			Root<Fornecedor> fornecedor) {
		
		List<Predicate> predicates = new ArrayList<>();
		Fornecedor fornecedorConsulta = fornecedorFiltro.getFornecedor();
        
        Path<Long> idPath = null;
        Path<String> nomePath = null;
        Path<String> emailPath = null;
        Path<String> telefonePath = null;
        Path<String> cnpjPath = null;
        
        if(fornecedorConsulta.getId()!=null) {
        	idPath = fornecedor.get("id");
        	predicates.add(cb.equal(idPath, fornecedorConsulta.getId()));
        }
        if(fornecedorConsulta.getNome()!=null) {
        	nomePath = fornecedor.get("nome");
        	predicates.add(cb.like(cb.upper(nomePath), "%"+fornecedorConsulta.getNome().toUpperCase()+"%"));
        }
        if(fornecedorConsulta.getEmail()!=null) {
        	emailPath = fornecedor.get("email");
        	predicates.add(cb.like(cb.upper(emailPath), 
        			"%"+fornecedorConsulta.getEmail().toUpperCase()+"%"));
        }
        if(fornecedorConsulta.getTelefone()!=null) {
			
        	Expression<String> undesirables = cb.literal(Pattern.compile("[()-]").toString());
			Expression<String> replaceWith = cb.literal("");
			Expression<String> global = cb.literal("g");
			telefonePath = fornecedor.get("telefone");
			Expression<String> regExp = cb.function("regexp_replace", String.class, telefonePath, 
					undesirables, replaceWith,global);
			 
			predicates.add(cb.like(cb.trim(regExp), "%"+fornecedorConsulta.getTelefone().trim()+"%"));
        }
        
        if(fornecedorConsulta.getCnpj()!=null) {
			
        	Expression<String> undesirables = cb.literal(Pattern.compile("[.-\\/]").toString());
			Expression<String> replaceWith = cb.literal("");
			Expression<String> global = cb.literal("g");
			cnpjPath = fornecedor.get("cnpj");
			Expression<String> regExp = cb.function("regexp_replace", String.class, cnpjPath, 
					undesirables, replaceWith, global);
			 
			predicates.add(cb.like(cb.trim(regExp), "%"+fornecedorConsulta.getCnpj().trim()+"%"));
        }
        
        return predicates;
	}

}
