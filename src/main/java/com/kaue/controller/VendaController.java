package com.kaue.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kaue.dao.filter.VendaFilter;
import com.kaue.model.Produto;
import com.kaue.model.Venda;
import com.kaue.service.VendaService;

@Controller
@RequestMapping("/vendas")
public class VendaController {
	
	private static final String LISTAR_VIEW = "page/vendas/Listar";
	
	@Autowired
	private VendaService vendaService;
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") VendaFilter filtro) {
		if(!filtro.isAvancada()) {
			String termo = filtro.getTermo();
			if(termo!=null) {
				//filtro.getVenda().setDataVenda(termo);
				try {
					Long id = Long.parseLong(termo);
					filtro.getVenda().setId(id);
				} catch(NumberFormatException nfe) {}
			}
		}
		List<Venda> vendaList = vendaService.pesquisar(filtro);
		ModelAndView mv = new ModelAndView(LISTAR_VIEW);
		mv.addObject("venda", new Venda());
		mv.addObject("vendas", vendaList);
		return mv;
	}
	
	@RequestMapping(value = "/detalhes/{id}")
	public ModelAndView detalhes(@PathVariable("id") Venda venda) {
		ModelAndView mv = new ModelAndView(LISTAR_VIEW + " :: #modalDetalhesVenda");
		mv.addObject("venda", venda);
		return mv;
	}
	
}
