package com.kaue.controller;

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

import com.kaue.dao.filter.CategoriaFilter;
import com.kaue.dao.filter.FornecedorFilter;
import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.model.Categoria;
import com.kaue.model.Fornecedor;
import com.kaue.model.Lote;
import com.kaue.model.Produto;
import com.kaue.service.CategoriaService;
import com.kaue.service.FornecedorService;
import com.kaue.service.ProdutoService;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

	private static final String CADASTRAR_VIEW = "page/produto/Cadastrar";
	private static final String LISTAR_VIEW = "page/produto/Listar";
	
	private static final String LISTAR_LOTE_VIEW = "page/lote/Cadastrar";
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@RequestMapping("/novo")
	public ModelAndView showFormNovo() {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		Produto produto = new Produto();
		produto.setQuantidade(0);
		mv.addObject("produto", produto);
		return mv;
	}
	
	@RequestMapping("{id}")
	public ModelAndView showFormEditar(@PathVariable("id") Produto produto) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		mv.addObject("produto", produto);
		return mv;
	}
	
	@RequestMapping("{id}/lote")
	public ModelAndView showListarLote(@PathVariable("id") Produto produto) 
	{
		ModelAndView mv = new ModelAndView(LISTAR_LOTE_VIEW);
		Lote lote = new Lote();
		lote.setProduto(produto);
		mv.addObject("lote", lote);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Produto produto, Errors errors, RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			return CADASTRAR_VIEW;
		}
		try {
			String url = "";
			if(produto.getId()==null) {
				attributes.addFlashAttribute("mensagem", "Produto cadastrado com sucesso!");
				url = "redirect:/produtos/novo";
			} else {
				attributes.addFlashAttribute("mensagem", "Produto alterado com sucesso!");
				url = "redirect:/produtos/"+produto.getId();
			}
			produtoService.salvar(produto);
			return url;
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
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
		produtoService.excluir(id);
		attributes.addFlashAttribute("mensagem", "Produto excluído com sucesso!");
		return "redirect:/produtos";
	}
	
	@ModelAttribute
	public List<Categoria> todasCategorias(){
		List<Categoria> categoriaList = categoriaService.pesquisar(new CategoriaFilter());
		return categoriaList;
	}
	
	@ModelAttribute
	public List<Fornecedor> todosFornecedores() {
		List<Fornecedor> fornecedorList = fornecedorService.pesquisar(new FornecedorFilter());
		return fornecedorList;
	}
}
