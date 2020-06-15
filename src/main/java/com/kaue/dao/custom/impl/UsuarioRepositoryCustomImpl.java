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

import com.kaue.dao.custom.UsuarioRepositoryCustom;
import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.dao.filter.UsuarioFilter;
import com.kaue.model.Produto;
import com.kaue.model.Usuario;

public class UsuarioRepositoryCustomImpl implements UsuarioRepositoryCustom{

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public List<Usuario> findUsuarioByFiltro(UsuarioFilter usuarioFiltro) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Usuario> query = cb.createQuery(Usuario.class);
        Root<Usuario> usuario = query.from(Usuario.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(usuarioFiltro, cb, usuario);
        
        if(predicates.size()>0) {
        	query.select(usuario).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        } else {
        	query.select(usuario);
        }
        query.orderBy(cb.desc(usuario.get("id")));
        
        TypedQuery<Usuario> typedQuery = null;
        boolean isPaginated = usuarioFiltro.isPaginated();
        if(isPaginated) {
        	Long page = usuarioFiltro.getPage();
        	Long pageSize = usuarioFiltro.getPageSize();
        	int start = page.intValue()*pageSize.intValue();
        	typedQuery = entityManager.createQuery(query).setFirstResult(start).setMaxResults(pageSize.intValue());
        } else {
        	typedQuery = entityManager.createQuery(query);
        }
 
        return typedQuery.getResultList();
	}

	@Override
	public Long countUsuarioByFiltro(UsuarioFilter usuarioFiltro) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Usuario> usuario = query.from(Usuario.class);
        
        List<Predicate> predicates = prepararPredicadosDaConsulta(usuarioFiltro, cb, usuario);
        
        if(predicates.size()>0) {
        	query.select(cb.count(usuario)).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        } else {
        	query.select(cb.count(usuario));
        }
        
        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
 
        return typedQuery.getSingleResult();
	}
	
	private List<Predicate> prepararPredicadosDaConsulta(UsuarioFilter usuarioFiltro, 
			CriteriaBuilder cb, 
			Root<Usuario> usuario) {
		
		List<Predicate> predicates = new ArrayList<>();
		Usuario usuarioConsulta = usuarioFiltro.getUsuario();
        
        Path<Long> idPath = null;
        Path<String> nomePath = null;
        Path<String> emailPath = null;
        
        if(usuarioConsulta.getId()!=null) {
        	idPath = usuario.get("id");
        	predicates.add(cb.equal(idPath, usuarioConsulta.getId()));
        }
        if(usuarioConsulta.getNome()!=null) {
        	nomePath = usuario.get("nome");
        	predicates.add(cb.like(cb.upper(nomePath), "%"+usuarioConsulta.getNome().toUpperCase()+"%"));
        }
        if(usuarioConsulta.getEmail()!=null) {
        	emailPath = usuario.get("email");
        	predicates.add(cb.like(cb.upper(emailPath), 
        			"%"+usuarioConsulta.getEmail().toUpperCase()+"%"));
        }
        
        return predicates;
	}

}
