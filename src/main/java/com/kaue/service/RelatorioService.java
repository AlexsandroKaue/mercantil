package com.kaue.service;

import java.util.List;
import java.util.Map;

import com.kaue.model.Registro;

import net.sf.jasperreports.engine.JRException;

public interface RelatorioService {
	
	public void gerarRelatorioEmPdf(List<Registro> registroList, Map<String, Object> param, String origem, String destino)
			throws JRException;
	
	public void gerarRelatorioEmExcel(List<Registro> registroList, Map<String, Object> param, String origem, String destino)
			throws JRException;
}
