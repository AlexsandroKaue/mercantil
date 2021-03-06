package com.kaue.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kaue.dao.filter.CategoriaFilter;
import com.kaue.dao.filter.UsuarioFilter;
import com.kaue.dao.filter.VendaFilter;
import com.kaue.enumeration.StatusVenda;
import com.kaue.enumeration.TipoVenda;
import com.kaue.enumeration.Unitario;
import com.kaue.model.Categoria;
import com.kaue.model.Cliente;
import com.kaue.model.Produto;
import com.kaue.model.Usuario;
import com.kaue.model.Venda;
import com.kaue.service.UsuarioService;
import com.kaue.service.VendaService;
import com.kaue.util.HasValue;

@Controller
@RequestMapping("/vendas")
public class VendaController {
	
	private static final String LISTAR_VIEW = "page/vendas/Listar";
	
	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") VendaFilter filtro) {
		if(!filtro.isAvancada()) {
			String termo = filtro.getTermo();
			if(HasValue.execute(termo)) {
				try {
					Long id = Long.parseLong(termo.trim());
					filtro.getVenda().setId(id);
				} catch(NumberFormatException nfe) {
					filtro.getVenda().setUsuario(new Usuario());
					filtro.getVenda().getUsuario().setNome(termo);
					filtro.getVenda().setCliente(new Cliente());
					filtro.getVenda().getCliente().setNome(termo);
				}
			}
		}
		List<Venda> vendaList = vendaService.pesquisar(filtro);
		ModelAndView mv = new ModelAndView(LISTAR_VIEW);
		mv.addObject("venda", new Venda());
		mv.addObject("vendas", vendaList);
		return mv;
	}
	
	@RequestMapping(value = "/detalhes/{id}")
	public ModelAndView detalhes(@PathVariable("id") Venda venda) {
		ModelAndView mv = new ModelAndView(LISTAR_VIEW + " :: detalhesDaVenda");
		mv.addObject("venda", venda);
		return mv;
	}
	
	@RequestMapping(value = "/realizar/pagamento")
	public ModelAndView efetuarPagamento(@RequestBody Venda aux) {
		ModelAndView mv = new ModelAndView(LISTAR_VIEW + " :: #modalDetalhesVenda");
		Long id = aux.getId();
		Venda venda = vendaService.buscarPorId(id);
		if(aux.getSaldo().compareTo(venda.getTotal())>-1) {
			venda.setSaldo(aux.getSaldo());
			BigDecimal troco = venda.getSaldo().subtract(venda.getTotal());
			venda.setTroco(troco);
			venda.setStatus(StatusVenda.FINALIZADA);
			venda = vendaService.salvar(venda);
		}
		mv.addObject("venda", venda);
		
		return mv;
	}
	
	@RequestMapping(value = "/iniciar/pagamento/{id}")
	public ModelAndView iniciarPagamento(@PathVariable("id") Venda venda) {
		ModelAndView mv = new ModelAndView(LISTAR_VIEW + " :: #modalPagamento");
		mv.addObject("venda", venda);
		return mv;
	}
	
	@ModelAttribute
	public List<Usuario> todosUsuarios(){
		List<Usuario> usuarioList = usuarioService.pesquisar(new UsuarioFilter());
		return usuarioList;
	}
	
	@ModelAttribute
	public List<StatusVenda> todosStatus(){
		return Arrays.asList(StatusVenda.values());
	}
	
	@ModelAttribute
	public List<TipoVenda> todosTiposDeVenda(){
		return Arrays.asList(TipoVenda.values());
	}
	
}
