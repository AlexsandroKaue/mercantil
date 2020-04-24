package com.kaue.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.model.Titulo;

public interface TituloService extends JpaRepository<Titulo, Long>{

}
