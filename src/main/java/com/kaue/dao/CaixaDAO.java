package com.kaue.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaue.dao.custom.CaixaRepositoryCustom;
import com.kaue.model.Caixa;

public interface CaixaDAO extends JpaRepository<Caixa, Long>, CaixaRepositoryCustom{

}
