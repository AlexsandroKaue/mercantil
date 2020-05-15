package com.kaue.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kaue.dao.filter.FornecedorFilter;
import com.kaue.dao.filter.LoteFilter;
import com.kaue.model.Fornecedor;
import com.kaue.model.Lote;
import com.kaue.model.Produto;
import com.kaue.service.FornecedorService;
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
	
	@Autowired
	private FornecedorService fornecedorService;

	@RequestMapping("/novo")
	public ModelAndView showFormNovo(@RequestParam("produtoId") Produto produto) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		Lote lote = new Lote();
		lote.setProduto(produto);
		mv.addObject("lote", lote);
		return mv;
	}

	@RequestMapping("{id}")
	public ModelAndView showFormEditar(@PathVariable("id") Lote lote) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		mv.addObject("lote", lote);
		return mv;
	}
	
	@RequestMapping(value = "{id}/ajax", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, Object> showFormEditarAjax(@PathVariable("id") Lote lote) {
		Map<String, Object> map = new HashMap<>();
		
		if(lote!=null) {
			map.put("id", lote.getId());
			map.put("dataFabricacao", new SimpleDateFormat("dd/MM/yyyy").format(lote.getDataFabricacao()) ); 
			map.put("dataVencimento", new SimpleDateFormat("dd/MM/yyyy").format(lote.getDataVencimento()));
			map.put("valorCusto", new DecimalFormat("#,##0.00").format(lote.getValorCusto()));
			map.put("quantidade", lote.getQuantidade());
			map.put("fornecedor", lote.getFornecedor() != null ? lote.getFornecedor().getId() : null);
		}
		return map;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Lote lote, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return LISTAR_VIEW;
		}
		try {

			Integer incremento = obterIncrementoProduto(lote);
			
			Produto produto = lote.getProduto();
			if(incremento.intValue() != 0) { 
				produto.setQuantidade(produto.getQuantidade()+incremento);
			}
			if(produto.getLoteList()==null) produto.setLoteList(new ArrayList<Lote>());
			if(produto.getLoteList().contains(lote)) {
				produto.getLoteList().remove(lote);
			}
			produto.getLoteList().add(lote);
			produtoService.salvar(produto);
			
			/* loteService.salvar(lote); */
			attributes.addFlashAttribute("mensagem", "Lote cadastrado com sucesso!");
			return "redirect:/lotes?produtoId="+lote.getProduto().getId();
		} catch (DataIntegrityViolationException e) {
			errors.rejectValue("dataDeVencimento", null, e.getMessage());
			return LISTAR_VIEW;
		}
	}

	@RequestMapping
	public ModelAndView listar(@RequestParam("produtoId") Produto produto, @ModelAttribute("filtro") LoteFilter filtro) {
		/*
		 * List<Lote> loteList = produto.getLoteList();//loteService.pesquisar(filtro);
		 * if(loteList == null) loteList = new ArrayList<Lote>();
		 */
		ModelAndView mv = new ModelAndView(LISTAR_VIEW);
		Lote lote = new Lote();
		lote.setProduto(produto);
		mv.addObject("lote", lote);
		return mv;
	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable("id") Lote lote, RedirectAttributes attributes) {
		
		Produto produto = lote.getProduto();
		produto.setQuantidade(produto.getQuantidade()-lote.getQuantidade());
		produto.getLoteList().remove(lote);
		produtoService.salvar(produto);

		attributes.addFlashAttribute("mensagem", "Lote excluído com sucesso!");
		return "redirect:/lotes?produtoId="+lote.getProduto().getId();
	}
	
	@ModelAttribute
	public List<Fornecedor> todosFornecedores() {
		List<Fornecedor> fornecedorList = fornecedorService.pesquisar(new FornecedorFilter());
		return fornecedorList;
	}

	private Integer obterIncrementoProduto(Lote lote) {
		Integer incremento = 0;

		if (lote.getQuantidade() != null) {
			boolean isNew = lote.getId() == null ? true : false;

			if (isNew) {
				incremento = lote.getQuantidade();
			} else {
				Lote lotePersistido = loteService.buscarPorId(lote.getId());
				incremento = lote.getQuantidade() - lotePersistido.getQuantidade();
			}
		}

		return incremento;
	}
}
