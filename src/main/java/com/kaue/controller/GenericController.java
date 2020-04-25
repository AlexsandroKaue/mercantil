package com.kaue.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		mv.addObject("titulo", new Titulo());
		return mv;
	}
	
	@RequestMapping
	public ModelAndView lista() {
		List<Titulo> tituloList = tituloService.findAll();
		ModelAndView mv = new ModelAndView("page/titulos/lista");
		mv.addObject("titulos", tituloList);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes attributes){
		if(errors.hasErrors()) {
			return "page/titulos/novo";
		}
		tituloService.save(titulo);
		attributes.addFlashAttribute("mensagem", "Título cadastrado com sucesso!");
		return "redirect:/titulos/novo";
	}
	
	@ModelAttribute
	public List<StatusTitulo> todosStatusTitulo(){
		return Arrays.asList(StatusTitulo.values());
	}

}
