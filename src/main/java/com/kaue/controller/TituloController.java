package com.kaue.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kaue.dao.filter.TituloFilter;
import com.kaue.enumeration.StatusTitulo;
import com.kaue.model.Titulo;
import com.kaue.service.TituloService;

@Controller
@RequestMapping("/titulos")
public class TituloController {
	
	private static final String CADASTRAR_VIEW = "page/titulos/Cadastrar";
	private static final String LISTAR_VIEW = "page/titulos/Listar";
	
	@Autowired
	private TituloService tituloService;
	
	@RequestMapping("/novo")
	public ModelAndView showFormNovo(Titulo titulo) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		mv.addObject("titulo", new Titulo());
		return mv;
	}
	
	@RequestMapping("{codigo}")
	public ModelAndView showFormEditar(@PathVariable("codigo") Titulo titulo) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		/* Titulo titulo = tituloService.findById(codigo).orElse(null); */
		mv.addObject("titulo", titulo);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes attributes){
		if(errors.hasErrors()) {
			return CADASTRAR_VIEW;
		}
		try {
			tituloService.salvar(titulo);
			attributes.addFlashAttribute("mensagem", "Título cadastrado com sucesso!");
			return "redirect:/titulos/novo";
		} catch(DataIntegrityViolationException e) {
			errors.rejectValue("dataVencimento", null, e.getMessage());
			return CADASTRAR_VIEW;
		}
	}
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") TituloFilter filtro) {
		//List<Titulo> tituloList = tituloDAO.findAll(Sort.by(Sort.Direction.ASC, "codigo"));
		List<Titulo> tituloList = tituloService.pesquisar(filtro);
		ModelAndView mv = new ModelAndView(LISTAR_VIEW);
		mv.addObject("titulos", tituloList);
		return mv;
	}
	
	@RequestMapping(value = "{codigo}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		tituloService.excluir(codigo);
		attributes.addFlashAttribute("mensagem", "Título excluído com sucesso!");
		return "redirect:/titulos";
	}
	
	@RequestMapping(value = "/{codigo}/receber", method = RequestMethod.PUT)
	public @ResponseBody String receber(@PathVariable Long codigo) {
		return tituloService.receber(codigo);
	}
	
	@ModelAttribute
	public List<StatusTitulo> todosStatusTitulo(){
		return Arrays.asList(StatusTitulo.values());
	}

}
