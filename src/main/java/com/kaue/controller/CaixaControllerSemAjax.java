package com.kaue.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.enumeration.OpcoesDesconto;
import com.kaue.enumeration.StatusVenda;
import com.kaue.model.Categoria;
import com.kaue.model.Item;
import com.kaue.model.Produto;
import com.kaue.model.Registro;
import com.kaue.model.Venda;
import com.kaue.service.ProdutoService;
import com.kaue.service.RelatorioService;
import com.kaue.service.VendaService;

@Controller
@RequestMapping("/caixa2")
public class CaixaControllerSemAjax {

	static final String REGISTRADORA_VIEW = "page/caixa/Vender";
	static final String PAGAMENTO_VIEW = "page/caixa/Pagamento";
	static final String RECIBO_VIEW = "page/caixa/Recibo";
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private RelatorioService relatorioService;
	
	private Venda venda;
	
	@ModelAttribute
	public void addAttributes(Model model){
		//model.addAttribute("produtoList", new ArrayList<Produto>());
		//model.addAttribute("emptySearchError", false);
		//model.addAttribute("inclusaoVaziaErro", false);
		Item item = new Item();
		item.setProduto(new Produto());
		model.addAttribute("item", item);
		model.addAttribute("teste", "Teste de passagem de valor do thymeleaf para o javascript.");
	}
	
	@RequestMapping
	public ModelAndView showRegistradora() {
		venda = new Venda();
		
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW);
		
		inicializarVenda();
		
		venda.setItemList(new ArrayList<Item>());
		venda.setDesconto(OpcoesDesconto.ZERO); 
		venda.setSubtotal(new BigDecimal(0.0));
		venda.setValorDesconto(new BigDecimal(0.0));
		venda.setTotal(venda.getSubtotal());
		
		mv.addObject("venda", venda);
		
		return mv;
	}
	
	private void inicializarVenda() {
		venda.setStatus(StatusVenda.ABERTA);
		venda.setItemList(new ArrayList<Item>());
		venda.setDesconto(OpcoesDesconto.ZERO);
		venda.setValorDesconto(new BigDecimal(0.0));
		venda.setSubtotal(new BigDecimal(0.0));
		venda.setTotal(venda.getSubtotal());
		venda.setSaldo(BigDecimal.ZERO);
		venda.setDataVenda(new Date());
	}
	
	@RequestMapping(value = "/incluir/{codigo}")
	public ModelAndView incluirItem(@PathVariable("codigo") String codigo, Model model) {
		
		ModelAndView mv = null;
		mv = new ModelAndView(REGISTRADORA_VIEW+" :: #conteudo");
		
		if(venda.getItemList() == null) venda.setItemList(new ArrayList<Item>());
		
		Item item = null;
		if(!codigo.isEmpty()) {
			Produto produto = produtoService.buscarPorCodigo(codigo);
				
			if(produto != null) {
				boolean isNew = true;
				for(Item it : venda.getItemList()) {
					if(it.getProduto().getCodigo().equals(codigo)) {
						it.setQuantidade(it.getQuantidade().add(new BigDecimal(1)));
						isNew = false;
						item = it; break;
					}
				}
				if(isNew) {
					item = new Item();
					item.setProduto(produto);
					item.setValor(produto.getValorDeVenda());
					item.setQuantidade(new BigDecimal(1));
					
					venda.getItemList().add(item);
					item.setVenda(venda);
				}
			}
			
			BigDecimal subtotal = new BigDecimal(0.0);
			for(Item it : venda.getItemList()) {
				subtotal = subtotal.add(it.getValor().multiply(it.getQuantidade()));
			}
			venda.setSubtotal(subtotal);
			venda.setTotal(venda.getSubtotal());
			venda.setDesconto(OpcoesDesconto.ZERO);
			venda.setValorDesconto(new BigDecimal(0.0));
		}

		mv.addObject("venda", venda);
		mv.addObject("item", item);
		
	    return mv;
	}
	
	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public ModelAndView incluir(@RequestBody Item i) {// HttpServletRequest request
		
		String codigo = i.getProduto().getCodigo();
		BigDecimal quantidade = i.getQuantidade();
		
		ModelAndView mv = null;
		mv = new ModelAndView(REGISTRADORA_VIEW+" :: #conteudo");
		
		if(venda.getItemList() == null) venda.setItemList(new ArrayList<Item>());
		
		Item item = null;
		if(!codigo.isEmpty()) {
			Produto produto = produtoService.buscarPorCodigo(codigo);
				
			if(produto != null) {
				boolean isNew = true;
				for(Item it : venda.getItemList()) {
					if(it.getProduto().getCodigo().equals(codigo)) {
						it.setQuantidade(it.getQuantidade().add(quantidade));
						it.setValorTotal(it.getQuantidade().multiply(it.getValor()));
						isNew = false;
						item = it; break;
					}
				}

				if(isNew) {
					item = new Item();
					item.setProduto(produto);
					item.setValor(item.getProduto().getValorDeVenda());
					item.setValorTotal(quantidade.multiply(item.getValor()));
					item.setQuantidade(quantidade);
					
					venda.getItemList().add(item);
					item.setVenda(venda);
				} 

			}
			
			BigDecimal subtotal = new BigDecimal(0.0);
			for(Item it : venda.getItemList()) {
				subtotal = subtotal.add(it.getValorTotal());
			}
			venda.setSubtotal(subtotal);
			venda.setTotal(venda.getSubtotal());
			venda.setDesconto(OpcoesDesconto.ZERO);
			venda.setValorDesconto(new BigDecimal(0.0));
		}

		mv.addObject("venda", venda);
		mv.addObject("item", item);
		
	    return mv;
	}
	
	@RequestMapping(value = "/excluir/{codigo}")
	public ModelAndView excluirItem(@PathVariable("codigo") String codigo) {
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW+" :: #conteudo");
		
		Iterator<Item> itemIterator = venda.getItemList().iterator();
		Item item = null;
		while(itemIterator.hasNext()) {
			item = itemIterator.next();
			if(item.getProduto().getCodigo().equals(codigo)) {
				/*if(item.getQuantidade().compareTo(new BigDecimal(1))>=0) {
					item.setQuantidade(item.getQuantidade().subtract(new BigDecimal(1)));
				} else {*/
					itemIterator.remove();
					item.setVenda(null);
				/* } */
			}
		}
		
		BigDecimal subtotal = new BigDecimal(0.0);
		for(Item it : venda.getItemList()) {
			subtotal = subtotal.add(it.getValorTotal());
		}
		venda.setSubtotal(subtotal);
		venda.setTotal(venda.getSubtotal());
		venda.setDesconto(OpcoesDesconto.ZERO);
		venda.setValorDesconto(new BigDecimal(0.0));
		
		mv.addObject("venda", venda);
		mv.addObject("item", item);
		
	    return mv;
		
	}
	
	/*@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public ModelAndView incluirItem(@Validated Item item, Errors errors) {
		
		ModelAndView mv = null;
		mv = new ModelAndView(REGISTRADORA_VIEW);
		
		if(!errors.hasErrors()) {
			
			if(venda.getItemList() == null) venda.setItemList(new ArrayList<Item>());
			
			String codigo = item.getProduto().getCodigo();
					
			if(codigo != null) {
				Produto produto = produtoService.buscarPorCodigo(codigo);
				item.setProduto(produto);
				BigDecimal valor = item.getQuantidade().multiply(produto.getValorDeVenda());
				item.setValor(valor);
				
				venda.getItemList().add(item);
				item.setVenda(venda);
			}
			
			BigDecimal subtotal = new BigDecimal(0.0);
			for(Item it : venda.getItemList()) {
				subtotal = subtotal.add(it.getValor().multiply(it.getQuantidade()));
			}
			venda.setSubtotal(subtotal);
			venda.setTotal(venda.getSubtotal());
			venda.setDesconto(OpcoesDesconto.ZERO);
			venda.setValorDesconto(new BigDecimal(0.0));		
		}
		mv.addObject("venda", venda);
		mv.addObject("item", item);
	    return mv;
	}*/
	
	/*
	 * @RequestMapping(value = "/incluir", method = RequestMethod.POST) public
	 * ModelAndView getSearchUserProfiles(@RequestBody Item item, HttpServletRequest
	 * request) { String codigo = item.getProduto().getCodigo(); BigDecimal
	 * quantidade = item.getQuantidade();
	 * 
	 * // your logic next return null; }
	 */
	
	@RequestMapping(value = "/balanca/{codigo}")
	public ModelAndView mostrarBalanca(@PathVariable("codigo") String codigo, Model model) {
		
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW+" :: #modalBalanca");
		if(!codigo.isEmpty()) {
			Produto produto = produtoService.buscarPorCodigo(codigo);
			if(produto != null) {
				Item item = new Item();
				item.setProduto(produto);
				model.addAttribute("item", item);
			}
		}
		
	    return mv;
		
	}
		
	@RequestMapping(value = "/venda/{id}")
	public ModelAndView detalhesVenda(@PathVariable("id") Venda venda) {
		ModelAndView mv = new ModelAndView(RECIBO_VIEW);
		List<Item> itemList = new ArrayList<Item>(venda.getItemList());
		gerarRelatorio(itemList);
		mv.addObject("venda", venda);
		return mv;
	}
	
	/*@RequestMapping(value = "/pagamento", method = RequestMethod.POST)
	public ModelAndView iniciarPagamento(Venda venda, Errors  errors) {		
				
		if(venda.getSubtotal().compareTo(BigDecimal.ZERO)==0) {
			errors.rejectValue("subtotal", null, "Subtotal não pode ser 0");
		}
	
		if(errors.hasErrors()) {
			return new ModelAndView(REGISTRADORA_VIEW);
		}
		
		ModelAndView mv = new ModelAndView(PAGAMENTO_VIEW);
		/*Passagem da lista de itens do objeto venda construído no servidor
		para o recebido pelo POST do form, devido ao fato de não poder se obter
		a lista de itens não persistidos (sem id)  
		venda.setItemList(new ArrayList<Item>());
		for(Item item : this.venda.getItemList()) {
			venda.getItemList().add(item);
			item.setVenda(venda);
		}
		
		venda.setStatus(StatusVenda.ABERTA);
		venda.setDesconto(OpcoesDesconto.ZERO);
		venda.setValorDesconto(new BigDecimal(0.0));
		venda.setTotal(venda.getSubtotal());
		venda.setDataVenda(new Date());
		venda.setSaldo(BigDecimal.ZERO);
		venda = vendaService.salvar(venda);

		venda = vendaService.buscarPorId(venda.getId());
		
		mv.addObject("venda", venda);
		return mv;
	}*/
	
	@RequestMapping(value = "/pagamento", method = RequestMethod.PUT)
	public ModelAndView iniciarPagamento() {		
		
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW + " :: #conteudo");
		
		venda.setStatus(StatusVenda.ABERTA);
		venda.setDesconto(OpcoesDesconto.ZERO);
		venda.setValorDesconto(new BigDecimal(0.0));
		venda.setTotal(venda.getSubtotal());
		venda.setDataVenda(new Date());
		venda.setSaldo(BigDecimal.ZERO);
		venda = vendaService.salvar(venda);
		
		mv.addObject("venda", venda);
		return mv;
	}
	
	/*@RequestMapping(method = RequestMethod.POST)
	public String finalizarPagamento(@Validated Venda venda, Errors errors, RedirectAttributes attributes) {
		
		if(venda.getSaldo()!=null) {
			if(venda.getSaldo().compareTo(venda.getTotal())==-1) {
				errors.rejectValue("saldo", null, "Saldo não suficiente");
			}
		}
		
		if(errors.hasErrors()) {
			return PAGAMENTO_VIEW;
		} else {
			venda.setStatus(StatusVenda.FINALIZADA);
			BigDecimal troco = venda.getSaldo().subtract(venda.getTotal());
			venda.setTroco(troco);
			
			venda = vendaService.salvar(venda);
			attributes.addFlashAttribute("mensagem", "Venda concluída com sucesso!");
			return "redirect:/caixa/venda/"+venda.getId();
		}
	}*/
	
	@RequestMapping(method = RequestMethod.POST)
	public String finalizarPagamento(@Validated Venda venda, Errors errors, RedirectAttributes attributes) {
		
		
		if(venda.getSubtotal().compareTo(BigDecimal.ZERO)==0) {
			errors.rejectValue("subtotal", null, "Erro: Subtotal não pode ser 0,00");
		}
		
		if(venda.getSaldo()!=null) {
			if(venda.getSaldo().compareTo(venda.getTotal())==-1) {
				errors.rejectValue("saldo", null, "Erro: Saldo insuficiente");
			}
		}
		venda.setDataVenda(new Date());
		
		if(errors.hasErrors()) {
			return REGISTRADORA_VIEW;
		} else {
			for(Item item : venda.getItemList()) { 
				item.setVenda(venda);
			}
			venda.setStatus(StatusVenda.FINALIZADA);
			BigDecimal troco = venda.getSaldo().subtract(venda.getTotal());
			venda.setTroco(troco);
			
			venda = vendaService.salvar(venda);
			attributes.addFlashAttribute("mensagem", "Venda concluída com sucesso!");
			return "redirect:/caixa/venda/"+venda.getId();
		}
	}
	
	/*@RequestMapping(value = "{venda}/desconto/{desconto}")
	public ModelAndView aplicarDesconto(@PathVariable("venda") Venda venda, @PathVariable("desconto") OpcoesDesconto opcao) {		
		
		ModelAndView mv = new ModelAndView(PAGAMENTO_VIEW + " :: #conteudo");
		venda.setDesconto(opcao);
		BigDecimal valorDesconto = venda.getSubtotal().multiply(new BigDecimal(venda.getDesconto().getNumero())).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal total = venda.getSubtotal().subtract(valorDesconto);
		venda.setValorDesconto(valorDesconto);
		venda.setTotal(total);

		mv.addObject("venda", venda);
		return mv;
	}*/
	
	@RequestMapping(value = "/desconto/{desconto}")
	public ModelAndView aplicarDesconto(@PathVariable("desconto") OpcoesDesconto opcao) {		
		
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW + " :: #modal-pagamento");
		venda.setDesconto(opcao);
		BigDecimal valorDesconto = venda.getSubtotal().multiply(new BigDecimal(venda.getDesconto().getNumero())).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal total = venda.getSubtotal().subtract(valorDesconto);
		venda.setValorDesconto(valorDesconto);
		venda.setTotal(total);

		mv.addObject("venda", venda);
		return mv;
	}
	
	@RequestMapping(value = "/buscarItem", produces = "application/json")
	public @ResponseBody Map<String, Object> obterProdutosAjax(@RequestParam("q") String q, @RequestParam("page") Integer page) throws IOException {
		
		String termo = q;
		List<Produto> produtoList = new ArrayList<Produto>();
		Long quantidadeDeProdutos = 0L;
		Long pageSize = 3L;
		if(termo != null && !termo.trim().isEmpty()) {
			ProdutoFilter filtro = new ProdutoFilter();

			try {
				Integer codigo = Integer.parseInt(termo);
				filtro.setCodigo(String.format("%010d" , codigo));
			} catch(NumberFormatException nfe) {}

			filtro.setDescricao(termo);
			filtro.setCategoria(new Categoria());
			filtro.getCategoria().setDescricao(termo);
			filtro.setPage(new Long(page.intValue()-1));
			filtro.setPageSize(pageSize);
			
			produtoList = produtoService.pesquisar(filtro);
			if(produtoList != null && produtoList.size()>0) {
				for(Produto produto : produtoList) {
					byte[] bytes = produto.getImagem();
					if(bytes == null) {
						File file = ResourceUtils.getFile("classpath:static/custom/img/produto/sem-imagem_2.jpg");
						Path path = file.toPath();
						bytes = Files.readAllBytes(path);
					}
					String encodedfile = new String(Base64.getEncoder().encode(bytes), "UTF-8");
					produto.setImagemBase64(encodedfile);
				}
			}
			
			quantidadeDeProdutos = produtoService.contar(filtro);
		} 
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("items", produtoList);
		data.put("total_count", quantidadeDeProdutos);
		data.put("page_size", pageSize);
		
		return data;
	}
	
	@ModelAttribute
	public List<OpcoesDesconto> todasOpcoesDesconto(){
		return Arrays.asList(OpcoesDesconto.values());
	}
	
	public void gerarRelatorio(List<Item> itemList) {
		try {
			List<Registro> registroList = new ArrayList<Registro>();
			Registro registro = null;
			for(Item item : itemList) {
				registro = new Registro();
				registro.setCodigo(item.getProduto().getCodigo());
				registro.setDescricao(item.getProduto().getDescricao().toUpperCase());
				registro.setQuantidade(item.getQuantidade().toString());
				registro.setValorUnitario(item.getValor().toString());
				registro.setValorItem(item.getValor().multiply(item.getQuantidade()).toString());
				registroList.add(registro);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("valorTotalItem", venda.getSubtotal().toString());
			param.put("valorDesconto", venda.getValorDesconto().toString());
			param.put("valorTotal", venda.getTotal().toString());
			
			String path = ResourceUtils.getFile("classpath:jrxml").getAbsolutePath();
			String origem = path + "/cupom.jrxml";
			String destino = path + "/cupom.pdf";
			//relatorioService.gerarRelatorioEmPdf(registroList, param, origem, destino);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
