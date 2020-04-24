package com.kaue.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.kaue.model.StatusTitulo;
import com.kaue.model.Titulo;
import com.kaue.service.TituloService;

@Controller
@RequestMapping("/titulos")
public class GenericController {
	
	@Autowired
	private TituloService tituloService;
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/index2")
	public String index2() {
		return "index2";
	}
	
	@RequestMapping("/index3")
	public String index3() {
		return "index3";
	}
	
	@RequestMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("page/titulos/novo");
		mv.addObject("todosStatusTitulo", StatusTitulo.values());
		return mv;
	}
	
	@RequestMapping
	public ModelAndView lista() {
		List<Titulo> tituloList = tituloService.findAll();
		ModelAndView mv = new ModelAndView("page/titulos/lista");
		mv.addObject("titulos", tituloList);
		return mv;
	}
	
	@RequestMapping(/* value = "/titulos", */  method = RequestMethod.POST)
	public ModelAndView salvar(Titulo titulo){
		tituloService.save(titulo);
		
		ModelAndView mv = new ModelAndView("page/titulos/novo");
		mv.addObject("mensagem", "Título cadastrado com sucesso!");
		return mv;
	}

}
