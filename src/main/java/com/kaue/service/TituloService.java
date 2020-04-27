package com.kaue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.kaue.dao.TituloDAO;
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

}
