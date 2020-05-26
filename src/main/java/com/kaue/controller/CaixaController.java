package com.kaue.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kaue.model.Item;
import com.kaue.model.Produto;
import com.kaue.model.Venda;
import com.kaue.service.ProdutoService;
import com.kaue.service.VendaService;

@Controller
@RequestMapping("/caixa")
public class CaixaController {

	static final String REGISTRADORA_VIEW = "page/caixa/Vender";
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private ProdutoService produtoService;
	
	private Venda venda = new Venda();
	
	@RequestMapping
	public ModelAndView showRegistradora() {
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
		mv.addObject("caixa", venda);
		return mv;
	}
	
	@RequestMapping(value = "/incluir/{codigo}", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public List<Item> incluirItem(@PathVariable("codigo") String codigo) {
		Item item = new Item();
		Produto produto = produtoService.buscarPorCodigo(codigo);
		if(produto != null) {
			item.setProduto(produto);
			item.setVenda(venda);
			
			if(venda.getItemList()==null) venda.setItemList(new ArrayList<Item>());
			venda.getItemList().add(item);
		}
		
		List<Item> itemList = venda.getItemList();
		return itemList;
		
	}
	
	@RequestMapping(value = "/excluir/{codigo}", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public List<Item> excluirItem(@PathVariable("codigo") String codigo) {
		
		Iterator<Item> itemIterator = venda.getItemList().iterator();
		Item item;
		while(itemIterator.hasNext()) {
			item = itemIterator.next();
			if(item.getProduto().getCodigo().equals(codigo)) {
				itemIterator.remove();
			}
		}
		
		List<Item> itemList = venda.getItemList();
		return itemList;
		
	}
	
	
}
