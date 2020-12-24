package com.kaue.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kaue.dao.filter.CadernetaFilter;
import com.kaue.model.Caderneta;
import com.kaue.service.CadernetaService;

@Controller
@RequestMapping("/caderneta")
public class CadernetaController {
	
	private static final String LISTAR_VIEW = "page/caderneta/Listar";
	
	@Autowired
	private CadernetaService cadernetaService;
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") CadernetaFilter filtro) {
		if(!filtro.isAvancada()) {
			String termo = filtro.getTermo();
			if(termo!=null) {
				//filtro.getCaderneta().setDataCaderneta(termo);
				try {
					Long id = Long.parseLong(termo);
					filtro.getCaderneta().setId(id);
				} catch(NumberFormatException nfe) {}
			}
		}
		List<Caderneta> cadernetaList = cadernetaService.pesquisar(filtro);
		ModelAndView mv = new ModelAndView(LISTAR_VIEW);
		mv.addObject("caderneta", new Caderneta());
		mv.addObject("cadernetas", cadernetaList);
		return mv;
	}
	
	@RequestMapping(value = "/detalhes/{id}")
	public ModelAndView detalhes(@PathVariable("id") Caderneta caderneta) {
		ModelAndView mv = new ModelAndView(LISTAR_VIEW + " :: #modalDetalhesCaderneta");
		mv.addObject("caderneta", caderneta);
		return mv;
	}
	
}
