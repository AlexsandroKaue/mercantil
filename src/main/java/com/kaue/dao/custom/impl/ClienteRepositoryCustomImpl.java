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

import com.kaue.dao.custom.ClienteRepositoryCustom;
import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.dao.filter.ClienteFilter;
import com.kaue.model.Produto;
import com.kaue.model.Cliente;

public class ClienteRepositoryCustomImpl implements ClienteRepositoryCustom{

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public List<Cliente> findClienteByFiltro(ClienteFilter clienteFiltro) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> query = cb.createQuery(Cliente.class);
        Root<Cliente> cliente = query.from(Cliente.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(clienteFiltro, cb, cliente);
        
        if(predicates.size()>0) {
        	query.select(cliente).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        } else {
        	query.select(cliente);
        }
        query.orderBy(cb.desc(cliente.get("id")));
        
        TypedQuery<Cliente> typedQuery = null;
        boolean isPaginated = clienteFiltro.isPaginated();
        if(isPaginated) {
        	Long page = clienteFiltro.getPage();
        	Long pageSize = clienteFiltro.getPageSize();
        	int start = page.intValue()*pageSize.intValue();
        	typedQuery = entityManager.createQuery(query).setFirstResult(start).setMaxResults(pageSize.intValue());
        } else {
        	typedQuery = entityManager.createQuery(query);
        }
 
        return typedQuery.getResultList();
	}

	@Override
	public Long countClienteByFiltro(ClienteFilter clienteFiltro) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Cliente> cliente = query.from(Cliente.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(clienteFiltro, cb, cliente);
        
        if(predicates.size()>0) {
        	query.select(cb.count(cliente)).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        } else {
        	query.select(cb.count(cliente));
        }
        
        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
 
        return typedQuery.getSingleResult();
	}
	
	private List<Predicate> prepararPredicadosDaConsulta(ClienteFilter clienteFiltro, 
			CriteriaBuilder cb, 
			Root<Cliente> cliente) {
		
		List<Predicate> predicates = new ArrayList<>();
		Cliente clienteConsulta = clienteFiltro.getCliente();
        
        Path<Long> idPath = null;
        Path<String> nomePath = null;
        Path<String> emailPath = null;
        
        if(clienteConsulta.getId()!=null) {
        	idPath = cliente.get("id");
        	predicates.add(cb.equal(idPath, clienteConsulta.getId()));
        }
        if(clienteConsulta.getNome()!=null) {
        	nomePath = cliente.get("nome");
        	predicates.add(cb.like(cb.upper(nomePath), "%"+clienteConsulta.getNome().toUpperCase()+"%"));
        }
        if(clienteConsulta.getEmail()!=null) {
        	emailPath = cliente.get("email");
        	predicates.add(cb.like(cb.upper(emailPath), 
        			"%"+clienteConsulta.getEmail().toUpperCase()+"%"));
        }
        
        return predicates;
	}

}
