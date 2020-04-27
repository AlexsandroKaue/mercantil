package com.kaue.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	public static final String NOVO_TITULO_VIEW = "page/titulos/novo";
	
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
		ModelAndView mv = new ModelAndView(NOVO_TITULO_VIEW);
		mv.addObject("titulo", new Titulo());
		return mv;
	}
	
	@RequestMapping
	public ModelAndView lista() {
		List<Titulo> tituloList = tituloService.findAll(Sort.by(Sort.Direction.ASC, "codigo"));
		ModelAndView mv = new ModelAndView("page/titulos/lista");
		mv.addObject("titulos", tituloList);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes attributes){
		if(errors.hasErrors()) {
			return NOVO_TITULO_VIEW;
		}
		try {
			tituloService.save(titulo);
			attributes.addFlashAttribute("mensagem", "Título cadastrado com sucesso!");
			return "redirect:/titulos/novo";
		} catch(DataIntegrityViolationException e) {
			errors.rejectValue("dataVencimento", null, "Formato de data inválido");
			return NOVO_TITULO_VIEW;
		}
	}
	
	@RequestMapping("{codigo}")
	public ModelAndView showFormEditar(@PathVariable("codigo") Titulo titulo) {
		ModelAndView mv = new ModelAndView(NOVO_TITULO_VIEW);
		/* Titulo titulo = tituloService.findById(codigo).orElse(null); */
		mv.addObject("titulo", titulo);
		return mv;
	}
	
	@RequestMapping(value = "{codigo}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		tituloService.deleteById(codigo);
		attributes.addFlashAttribute("mensagem", "Título excluído com sucesso!");
		return "redirect:/titulos";
	}
	
	@ModelAttribute
	public List<StatusTitulo> todosStatusTitulo(){
		return Arrays.asList(StatusTitulo.values());
	}

}
