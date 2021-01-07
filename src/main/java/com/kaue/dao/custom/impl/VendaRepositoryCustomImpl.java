package com.kaue.dao.custom.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.Trimspec;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.kaue.dao.custom.VendaRepositoryCustom;
import com.kaue.dao.filter.VendaFilter;
import com.kaue.enumeration.StatusVenda;
import com.kaue.enumeration.TipoVenda;
import com.kaue.model.Cliente;
import com.kaue.model.Usuario;
import com.kaue.model.Venda;
import com.kaue.util.HasValue;

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
        Path<String> usuarioPath = null;
        Path<String> clientePath = null;
        Path<Long> clienteIdPath = null;
        Path<Date> dataInicialPath = null;
        Path<Date> dataFinalPath = null;
        Path<StatusVenda> statusPath = null;
        Path<TipoVenda> tipoVendaPath = null;
        
        
        if(HasValue.execute(vendaFiltro.getVenda().getId())) {
        	idPath = venda.get("id");
        	predicates.add(cb.equal(idPath, vendaConsulta.getId()));
        }
        if(HasValue.execute(vendaFiltro.getVenda().getUsuario())) {
        	if(HasValue.execute(vendaFiltro.getVenda().getUsuario().getNome())) {
            	usuarioPath = venda.get("usuario").get("nome");
            	if(HasValue.execute(usuarioPath)) {
            		predicates.add(cb.like(
            				cb.upper(cb.trim(Trimspec.BOTH, usuarioPath)), "%"+vendaFiltro.getVenda().getUsuario().getNome().toUpperCase().trim()+"%"
            			)
            		);
            	}
        	}
        }
        
        if(HasValue.execute(vendaFiltro.getVenda().getCliente())) {
        	if(HasValue.execute(vendaFiltro.getVenda().getCliente().getId())) {
        		clienteIdPath = venda.get("cliente").get("id");
        		predicates.add(cb.equal(clienteIdPath, vendaConsulta.getCliente().getId()));
        	} else if(HasValue.execute(vendaFiltro.getVenda().getCliente().getNome())) {
        		//clientePath = venda.get("cliente") != null ? venda.get("cliente").get("nome") : null;
            	clientePath = venda.get("cliente").get("nome");
            	predicates.add(cb.like(
        				cb.upper(cb.trim(Trimspec.BOTH, clientePath)), 
        				"%"+vendaFiltro.getVenda().getCliente().getNome().toUpperCase()+"%"
        			)
        		);
            	if(HasValue.execute(clientePath)) {
            		predicates.add(cb.like(
            				cb.upper(cb.trim(Trimspec.BOTH, clientePath)), 
            				"%"+vendaFiltro.getVenda().getCliente().getNome().toUpperCase()+"%"
            			)
            		);
            	}
        	}
        }
        
        if(HasValue.execute(vendaFiltro.getDataInicio())) {
        	dataInicialPath = venda.get("dataVenda");
        	predicates.add(cb.greaterThanOrEqualTo(dataInicialPath, vendaFiltro.getDataInicio()));
        }
        if(HasValue.execute(vendaFiltro.getDataFim())) {
        	dataFinalPath = venda.get("dataVenda");
        	predicates.add(cb.lessThanOrEqualTo(dataFinalPath, vendaFiltro.getDataFim()));
        }
        
        if(HasValue.execute(vendaFiltro.getVenda().getStatus())) {
        	statusPath = venda.get("status");
        	predicates.add(cb.equal(statusPath, vendaFiltro.getVenda().getStatus()));
        }
        
        if(HasValue.execute(vendaFiltro.getVenda().getTipoVenda())) {
        	tipoVendaPath = venda.get("tipoVenda");
        	predicates.add(cb.equal(tipoVendaPath, vendaFiltro.getVenda().getTipoVenda()));
        }
        
        return predicates;
	}

}
