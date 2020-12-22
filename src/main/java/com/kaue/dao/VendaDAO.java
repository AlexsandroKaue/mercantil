package com.kaue.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.dao.custom.VendaRepositoryCustom;
import com.kaue.model.Venda;

public interface VendaDAO extends JpaRepository<Venda, Long>, VendaRepositoryCustom{

	//public List<Venda> findByDescricaoContainingIgnoreCaseOrderByIdDesc(String descricao);

}
