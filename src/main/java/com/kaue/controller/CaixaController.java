package com.kaue.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
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

import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.enumeration.OpcoesDesconto;
import com.kaue.enumeration.StatusVenda;
import com.kaue.model.Categoria;
import com.kaue.model.Registro;
import com.kaue.model.Fornecedor;
import com.kaue.model.Item;
import com.kaue.model.Produto;
import com.kaue.model.Venda;
import com.kaue.service.ProdutoService;
import com.kaue.service.RelatorioService;
import com.kaue.service.VendaService;

@Controller
@RequestMapping("/caixa")
public class CaixaController {

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
	
	@RequestMapping
	public ModelAndView showRegistradora() {
		venda = new Venda();
		Resource resource = resourceLoader.getResource("classpath:registro.txt");
		File file = null;
		String registro = null;
		try {
			file = resource.getFile();
			registro = new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
						it.setQuantidade(it.getQuantidade()+1);
						isNew = false;
						item = it; break;
					}
				}
				if(isNew) {
					item = new Item();
					item.setProduto(produto);
					item.setValor(produto.getValorDeVenda());
					item.setVenda(venda);
					item.setQuantidade(1);
					
					venda.getItemList().add(item);
				}
			}
			
			BigDecimal subtotal = new BigDecimal(0.0);
			for(Item it : venda.getItemList()) {
				subtotal = subtotal.add(it.getValor().multiply(new BigDecimal(it.getQuantidade())));
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
				if(item.getQuantidade()>1) {
					item.setQuantidade(item.getQuantidade()-1);
				} else {
					itemIterator.remove();
				}
			}
		}
		
		BigDecimal subtotal = new BigDecimal(0.0);
		for(Item it : venda.getItemList()) {
			subtotal = subtotal.add(it.getValor().multiply(new BigDecimal(it.getQuantidade())));
		}
		venda.setSubtotal(subtotal);
		venda.setTotal(venda.getSubtotal());
		venda.setDesconto(OpcoesDesconto.ZERO);
		venda.setValorDesconto(new BigDecimal(0.0));
		
		mv.addObject("venda", venda);
		mv.addObject("item", item);
		
	    return mv;
		
	}
		
	@RequestMapping(value = "/venda/{id}")
	public ModelAndView detalhesVenda(@PathVariable("id") Venda venda) {
		ModelAndView mv = new ModelAndView(RECIBO_VIEW);
		gerarRelatorio(venda.getItemList());
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
		
		venda.setDataVenda(new Date());
		if(venda.getSubtotal().compareTo(BigDecimal.ZERO)==0) {
			errors.rejectValue("subtotal", null, "Erro: Subtotal não pode ser 0,00");
		}
		
		if(venda.getSaldo()!=null) {
			if(venda.getSaldo().compareTo(venda.getTotal())==-1) {
				errors.rejectValue("saldo", null, "Erro: Saldo insuficiente");
			}
		}
		
		if(errors.hasErrors()) {
			return REGISTRADORA_VIEW;
		} else {
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
		
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW + " :: #conteudo");
		venda.setDesconto(opcao);
		BigDecimal valorDesconto = venda.getSubtotal().multiply(new BigDecimal(venda.getDesconto().getNumero())).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal total = venda.getSubtotal().subtract(valorDesconto);
		venda.setValorDesconto(valorDesconto);
		venda.setTotal(total);

		mv.addObject("venda", venda);
		return mv;
	}
	
	@RequestMapping(value = "/buscarItem", produces = "application/json")
	public @ResponseBody Map<String, Object> obterProdutosAjax(@RequestParam("q") String q, @RequestParam("page") Integer page) {
		
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
	
	@ModelAttribute
	public void addAttributes(Model model){
		model.addAttribute("produtoList", new ArrayList<Produto>());
		model.addAttribute("emptySearchError", false);
		model.addAttribute("inclusaoVaziaErro", false);
	}
	
	public void gerarRelatorio(List<Item> itemList) {
		try {
			List<Registro> registroList = new ArrayList<Registro>();
			Registro registro = null;
			for(Item item : itemList) {
				registro = new Registro();
				registro.setCodigo(item.getProduto().getCodigo());
				registro.setDescricao(item.getProduto().getDescricao());
				registro.setQuantidade(Integer.toString(item.getQuantidade()));
				registroList.add(registro);
			}
			
			String nomeArquivo = "cupom";
			String formatoSaida = "pdf";
			String origem = getRealPath(new File("resources/jrxml/" + nomeArquivo + ".jrxml").getPath());
			String destino = getRealPath(new File("resources/jrxml/" + nomeArquivo+ "." + formatoSaida).getPath());

			try {
				Resource resourceOrigem = resourceLoader.getResource("classpath:static/jrxml/cupom.jrxml");
				File file = resourceOrigem.getFile();
				String origem = file.getAbsolutePath();
				String destino = new File(ResourceUtils.getFile("classpath:static/jrxml").getAbsolutePath()+"")
				relatorioService.gerarRelatorioEmPdf(registroList, null, origem, destino);
				setRelatorioPdf(new DefaultStreamedContent(new FileInputStream(destino), formatoSaida, nomeArquivo+"."+formatoSaida));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getRealPath(final String caminho) {
		final String requestContextPath = servletContext.getRealPath("/");
		final String path = requestContextPath + caminho;
		return path;
	}
	
	
	
	
}
