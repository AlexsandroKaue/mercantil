package com.kaue.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kaue.dao.filter.CategoriaFilter;
import com.kaue.model.Categoria;
import com.kaue.service.CategoriaService;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {
	
	private static final String CADASTRAR_VIEW = "page/categoria/Cadastrar";
	private static final String LISTAR_VIEW = "page/categoria/Listar";
	
	@Autowired
	private CategoriaService categoriaService;
	
	@RequestMapping("/novo")
	public ModelAndView showFormNovo(Categoria categoria) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		mv.addObject("categoria", new Categoria());
		return mv;
	}
	
	@RequestMapping("{id}")
	public ModelAndView showFormEditar(@PathVariable("id") Categoria categoria) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		/* Categoria categoria = categoriaService.findById(id).orElse(null); */
		mv.addObject("categoria", categoria);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Categoria categoria, Errors errors, RedirectAttributes attributes){
		if(errors.hasErrors()) {
			return CADASTRAR_VIEW;
		}
		categoriaService.salvar(categoria);
		attributes.addFlashAttribute("mensagem", "Categoria cadastrado com sucesso!");
		return "redirect:/categorias/novo";
	}
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") CategoriaFilter filtro) {
		//List<Categoria> categoriaList = categoriaDAO.findAll(Sort.by(Sort.Direction.ASC, "id"));
		List<Categoria> categoriaList = categoriaService.pesquisar(filtro);
		ModelAndView mv = new ModelAndView(LISTAR_VIEW);
		mv.addObject("categorias", categoriaList);
		return mv;
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
		categoriaService.excluir(id);
		attributes.addFlashAttribute("mensagem", "Título excluído com sucesso!");
		return "redirect:/categorias";
	}

}
