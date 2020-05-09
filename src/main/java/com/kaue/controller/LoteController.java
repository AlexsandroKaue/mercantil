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

import com.kaue.dao.filter.LoteFilter;
import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.model.Lote;
import com.kaue.model.Produto;
import com.kaue.service.LoteService;
import com.kaue.service.ProdutoService;

@Controller
@RequestMapping("/lotes")
public class LoteController {

	private static final String CADASTRAR_VIEW = "page/lote/Cadastrar";
	private static final String LISTAR_VIEW = "page/lote/Listar";
	
	@Autowired
	private LoteService loteService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@RequestMapping("/novo")
	public ModelAndView showFormNovo(Lote lote) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		mv.addObject("lote", new Lote());
		return mv;
	}
	
	@RequestMapping("{id}")
	public ModelAndView showFormEditar(@PathVariable("id") Lote lote) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		mv.addObject("lote", lote);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Lote lote, Errors errors, RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			return CADASTRAR_VIEW;
		}
		try {
			loteService.salvar(lote);
			attributes.addFlashAttribute("mensagem", "Lote cadastrado com sucesso!");
			return "redirect:/produtos/"+lote.getProduto().getId()+"/lote";
		} catch(DataIntegrityViolationException e) {
			errors.rejectValue("dataDeVencimento", null, e.getMessage());
			return CADASTRAR_VIEW;
		}
	}
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") LoteFilter filtro) {
		List<Lote> loteList = loteService.pesquisar(filtro);
		ModelAndView mv = new ModelAndView(LISTAR_VIEW);
		mv.addObject("lotes", loteList);
		return mv;
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
		Produto produto = loteService.buscarPorId(id).getProduto();
		loteService.excluir(id);
		attributes.addFlashAttribute("mensagem", "Lote excluído com sucesso!");
		return "redirect:/produtos/"+produto.getId()+"/lote";
	}
	
	@ModelAttribute
	public List<Produto> todasCategorias(){
		List<Produto> produtoList = produtoService.pesquisar(new ProdutoFilter());
		return produtoList;
	}
}
