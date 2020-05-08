package com.kaue.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kaue.model.Lote;
import com.kaue.model.Permissao;

public interface LoteDAO extends JpaRepository<Lote, Long>{	
	@Query("select l from Lote l")
	public List<Lote> findAllAndSort(Sort sort);
}
