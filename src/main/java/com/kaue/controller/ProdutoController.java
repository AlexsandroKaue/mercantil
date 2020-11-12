package com.kaue.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kaue.dao.filter.CategoriaFilter;
import com.kaue.dao.filter.FornecedorFilter;
import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.enumeration.Unitario;
import com.kaue.model.Categoria;
import com.kaue.model.Fornecedor;
import com.kaue.model.Lote;
import com.kaue.model.Produto;
import com.kaue.service.CategoriaService;
import com.kaue.service.FornecedorService;
import com.kaue.service.ProdutoService;
import com.kaue.util.HasValue;

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
	
	@ModelAttribute
	public void addAttributes(Model model){
		model.addAttribute("imagem", "/custom/img/produto/sem-imagem_2.jpg");
	}
	
	@RequestMapping("/novo")
	public ModelAndView showFormNovo() {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		Produto produto = new Produto();
		produto.setQuantidade(0);
		produto.setImagemBase64(produtoService.carregarImagem(null));
		mv.addObject("produto", produto);
		return mv;
	}
	
	@RequestMapping("{id}")
	public ModelAndView showFormEditar(@PathVariable("id") Produto produto) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		produto.setImagemBase64(produtoService.carregarImagem(produto.getImagemPath()));
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
	public String salvar(@Validated Produto produto, Errors errors, @RequestParam("file") MultipartFile multipartFile, 
			RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			return CADASTRAR_VIEW;
		}
		try {
			String url = "";
			if(produto.getId()==null) {
				attributes.addFlashAttribute("mensagem", "Produto cadastrado com sucesso!");
				Long idAtual = produtoService.obterIdAtual();
				produto.setCodigo(String.format("%010d" , idAtual+1));
				url = "redirect:/produtos/novo";
			} else {
				attributes.addFlashAttribute("mensagem", "Produto alterado com sucesso!");
				url = "redirect:/produtos/"+produto.getId();
			}
			
			
			boolean hasFileUploaded = !multipartFile.isEmpty();
			if(hasFileUploaded) {
				try {
					Long id;
					if(HasValue.execute(produto.getId())) {
						id = produto.getId();
					} else {
						id = produtoService.obterMaxId()+1;
					}
					
					String filename = produtoService.salvarImagem(multipartFile, "Produto_"+id);
					produto.setImagemPath(filename);
				} catch (Exception e) {
					attributes.addFlashAttribute("aviso", "Não foi possível salvar a imagem. Motivo: "+e.getMessage());
				}
			}
			
			produto = produtoService.salvar(produto);
			
			return url;
		} catch (Exception e) {
			errors.reject(null, e.getMessage());
			return CADASTRAR_VIEW;
		}
	}
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") ProdutoFilter filtro, 
			@PageableDefault(size = 10) Pageable pageable) {
		if(!filtro.isAvancada()) {
			String termo = filtro.getTermo();
			if(termo!=null) {
				filtro.getProduto().setDescricao(termo);
				filtro.getProduto().setMarca(termo);
				filtro.getProduto().setCategoria(new Categoria());
				filtro.getProduto().getCategoria().setDescricao(termo);
				try {
					Long id = Long.parseLong(termo);
					filtro.getProduto().setId(id);
				} catch(NumberFormatException nfe) {}
				/*
				 * filtro.setPaginated(true); filtro.setPage(new
				 * Long(pageable.getPageNumber())); filtro.setPageSize(new
				 * Long(pageable.getPageSize()));
				 */
			}
		}
		List<Produto> produtoList = produtoService.pesquisar(filtro);
		ModelAndView mv = new ModelAndView(LISTAR_VIEW);
		mv.addObject("produtos", produtoList);
		return mv;
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
		try {
			produtoService.excluir(id);
			attributes.addFlashAttribute("mensagem", "Produto excluído com sucesso!");
		} catch (Exception e) {
			attributes.addFlashAttribute("mensagem_erro", e.getMessage());
		}

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
	
	@ModelAttribute
	public List<Unitario> todosUnitarios(){
		return Arrays.asList(Unitario.values());
	}
}
