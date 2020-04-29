package com.kaue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.kaue.dao.TituloDAO;
import com.kaue.dao.filter.TituloFilter;
import com.kaue.enumeration.StatusTitulo;
import com.kaue.model.Titulo;

@Service
public class TituloService {
	
	@Autowired
	private TituloDAO tituloDAO;
	
	public void salvar(Titulo titulo) {
		try {
			tituloDAO.save(titulo);
		} catch(DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato de data inválido");
		}
	}
	
	public void excluir(Long codigo) {
		tituloDAO.deleteById(codigo);
	}

	public String receber(Long codigo) {
		Titulo titulo = tituloDAO.findById(codigo).orElse(null);
		titulo.setStatus(StatusTitulo.RECEBIDO);
		tituloDAO.save(titulo);
		return StatusTitulo.RECEBIDO.getDescricao();
	}
	
	public List<Titulo> pesquisar(TituloFilter filtro) {
		String descricao = (filtro.getDescricao() == null ? "" : filtro.getDescricao());
		return tituloDAO.findByDescricaoContainingIgnoreCaseOrderByCodigo(descricao);
	}

}
