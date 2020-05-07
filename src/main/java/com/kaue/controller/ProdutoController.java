package com.kaue.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.enumeration.Categoria;
import com.kaue.enumeration.StatusTitulo;
import com.kaue.model.Produto;
import com.kaue.model.Titulo;
import com.kaue.service.ProdutoService;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

	private static final String CADASTRAR_VIEW = "page/produto/Cadastrar";
	private static final String LISTAR_VIEW = "page/produto/Listar";
	
	@Autowired
	private ProdutoService produtoService;
	
	@RequestMapping("/novo")
	public ModelAndView showFormNovo(Produto produto) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		mv.addObject("produto", new Produto());
		return mv;
	}
	
	@RequestMapping("{codigo}")
	public ModelAndView showFormEditar(@PathVariable("codigo") Produto produto) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		mv.addObject("produto", produto);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Produto produto, Errors errors, RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			return CADASTRAR_VIEW;
		}
		try {
			produtoService.salvar(produto);
			attributes.addFlashAttribute("mensagem", "Produto cadastrado com sucesso!");
			return "redirect:/produtos/novo";
		} catch(DataIntegrityViolationException e) {
			errors.rejectValue("dataDeVencimento", null, e.getMessage());
			return CADASTRAR_VIEW;
		}
	}
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") ProdutoFilter filtro) {
		List<Produto> produtoList = produtoService.pesquisar(filtro);
		ModelAndView mv = new ModelAndView(LISTAR_VIEW);
		mv.addObject("produtos", produtoList);
		return mv;
	}
	
	@RequestMapping(value = "{codigo}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		produtoService.excluir(codigo);
		attributes.addFlashAttribute("mensagem", "Produto excluído com sucesso!");
		return "redirect:/produtos";
	}
	
	@ModelAttribute
	public List<Categoria> todasCategorias(){
		return Arrays.asList(Categoria.values());
	}
}
