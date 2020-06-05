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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kaue.dao.filter.ProdutoFilter;
import com.kaue.enumeration.OpcoesDesconto;
import com.kaue.enumeration.StatusTitulo;
import com.kaue.enumeration.StatusVenda;
import com.kaue.model.Categoria;
import com.kaue.model.Item;
import com.kaue.model.Produto;
import com.kaue.model.Venda;
import com.kaue.service.ProdutoService;
import com.kaue.service.VendaService;

@Controller
@RequestMapping("/caixa")
public class CaixaController {

	static final String REGISTRADORA_VIEW = "page/caixa/Vender";
	static final String VENDA_CONLUIDA_VIEW = "page/caixa/Recibo";
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private ProdutoService produtoService;
	
	private Venda venda;
	
	@RequestMapping
	public ModelAndView showRegistradora() {
		venda = new Venda();
		Resource resource = resourceLoader.getResource("classpath:cupom.txt");
		File file = null;
		String cupom = null;
		try {
			file = resource.getFile();
			cupom = new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ModelAndView mv = new ModelAndView(REGISTRADORA_VIEW);
		venda.setItemList(new ArrayList<Item>());
		venda.setDesconto(OpcoesDesconto.ZERO);
		mv.addObject("venda", venda);
		/* mv.addObject("listaDeItens", new ArrayList<Item>()); */
		mv.addObject("listaDeProdutos", new ArrayList<Produto>());
		return mv;
	}
	
	@RequestMapping(value = "/incluir/{codigo}", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, Object> incluirItem(@PathVariable("codigo") String codigo) {
		
		if(venda.getItemList() == null) venda.setItemList(new ArrayList<Item>());
		Item item = null;
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

		Map<String, Object> map = new HashMap<>();
		map.put("venda", venda);
		map.put("item", item);
		
		return map;
		
	}
	
	@RequestMapping(value = "/excluir/{codigo}", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, Object> excluirItem(@PathVariable("codigo") String codigo) {
		
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
		
		Map<String, Object> map = new HashMap<>();
		map.put("venda", venda);
		map.put("item", item);
		
		return map;
		
	}
	
	@RequestMapping(value = "/obterItens", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public List<Item> obterItens() {
		return venda.getItemList();
	}
	
	@RequestMapping(value = "/buscarProduto/{termo}", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public List<Produto> buscarProduto(@PathVariable("termo") String termo) {
		
		ProdutoFilter filtro = new ProdutoFilter();

		try {
			Integer codigo = Integer.parseInt(termo);
			filtro.setCodigo(String.format("%010d" , codigo));
		} catch(NumberFormatException nfe) {}

		filtro.setDescricao(termo);
		filtro.setCategoria(new Categoria());
		filtro.getCategoria().setDescricao(termo);
		
		List<Produto> produtoList = produtoService.pesquisar(filtro);
		
		return produtoList;
		
	}
	
	@RequestMapping(value = "/finalizarVenda")
	public String finalizarVenda() {
		
		venda.setDataVenda(new Date());
		venda.setStatus(StatusVenda.FINALIZADA);
		if(venda.getSaldo().compareTo(venda.getTotal()) > 0) {
			BigDecimal troco = venda.getSaldo().subtract(venda.getTotal());
			venda.setTroco(troco);
		}
		
		venda = vendaService.salvar(venda);

		return "redirect:/caixa/venda/"+venda.getId();
	}
	
	@RequestMapping(value = "/venda/{id}")
	public ModelAndView detalhesVenda(@PathVariable("id") Venda venda) {
		ModelAndView mv = new ModelAndView(VENDA_CONLUIDA_VIEW);
		mv.addObject("venda", venda);
		return mv;
	}
	
	@RequestMapping(value = "/pagar", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Venda pagar() {		
		
		venda.setDesconto(OpcoesDesconto.ZERO);
		venda.setTotal(venda.getSubtotal());

		return venda;
	}
	
	@RequestMapping(value = "/aplicar/desconto/{desconto}", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, Venda> aplicarDesconto(@PathVariable("desconto") OpcoesDesconto opcao) {		
		
		venda.setDesconto(opcao);
		BigDecimal valorDesconto = venda.getSubtotal().multiply(new BigDecimal(venda.getDesconto().getNumero())).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal total = venda.getSubtotal().subtract(valorDesconto);
		venda.setValorDesconto(valorDesconto);
		venda.setTotal(total);

		Map<String, Venda> map = new HashMap<String, Venda>();
		map.put("venda", venda);
		return map;
	}
	
	@RequestMapping(value = "/aplicar/saldo/{saldo}")
	public @ResponseBody void aplicarSaldo(@PathVariable("saldo") BigDecimal saldo) {	
		venda.setSaldo(saldo);
	}
	
	@ModelAttribute
	public List<OpcoesDesconto> todasOpcoesDesconto(){
		return Arrays.asList(OpcoesDesconto.values());
	}
	
}
