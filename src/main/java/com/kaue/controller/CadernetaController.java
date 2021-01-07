package com.kaue.controller;

import java.util.Arrays;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.kaue.dao.filter.ClienteFilter;
import com.kaue.dao.filter.UsuarioFilter;
import com.kaue.dao.filter.VendaFilter;
import com.kaue.enumeration.StatusVenda;
import com.kaue.model.Cliente;
import com.kaue.model.Endereco;
import com.kaue.model.Usuario;
import com.kaue.model.Venda;
import com.kaue.service.ClienteService;
import com.kaue.service.UsuarioService;
import com.kaue.service.VendaService;
import com.kaue.util.HasValue;

@Controller
@RequestMapping("/caderneta")
public class CadernetaController {
	
	private static final String LISTAR_VIEW = "page/caderneta/Listar";
	private static final String DETALHAR_VIEW = "page/caderneta/Detalhar";
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") ClienteFilter filtro) {
		if(!filtro.isAvancada()) {
			String termo = filtro.getTermo();
			if(termo!=null) {
				//filtro.getCaderneta().setDataCaderneta(termo);
				try {
					Long id = Long.parseLong(termo);
					filtro.getCliente().setId(id);
				} catch(NumberFormatException nfe) {}
			}
		}
		filtro.setOrder(Order.asc("nome"));
		List<Cliente> clienteList = clienteService.pesquisar(filtro);
		if(HasValue.execute(clienteList)) {
			for(Cliente cliente : clienteList) {
				String enderecoFormatado = "";
				if(cliente.getEndereco()!=null) {
					Endereco endereco = cliente.getEndereco();
					if(endereco.getLogradouro() != null && !endereco.getLogradouro().isEmpty()) {
						enderecoFormatado += endereco.getLogradouro()+", ";
					}
					if(endereco.getNumero() != null && !endereco.getNumero().isEmpty()) {
						enderecoFormatado += endereco.getNumero()+", ";
					}
					if(endereco.getBairro() != null && !endereco.getBairro().isEmpty()) {
						enderecoFormatado += endereco.getBairro()+", ";
					}
					if(endereco.getCep() != null && !endereco.getCep().isEmpty()) {
						enderecoFormatado += endereco.getCep()+", ";
					}
					if(endereco.getCidade() != null && !endereco.getCidade().isEmpty()) {
						enderecoFormatado += endereco.getCidade()+", ";
					}
					if(endereco.getEstado() != null) {
						enderecoFormatado += endereco.getEstado().getDescricao()+", ";
					}
					if(enderecoFormatado.length()>2) {
						enderecoFormatado = enderecoFormatado.substring(0, enderecoFormatado.length()-2);
					}
					
				}
				cliente.setEnderecoFormatado(enderecoFormatado);
			}
		}
		ModelAndView mv = new ModelAndView(LISTAR_VIEW);
		mv.addObject("clientes", clienteList);
		
		return mv;
	}

	@RequestMapping("/detalhes/{id}")
	public ModelAndView showDetalhesCaderneta(@PathVariable("id") Cliente cliente) {
		ModelAndView mv = new ModelAndView(DETALHAR_VIEW);
		mv.addObject("cliente", cliente);
		
		VendaFilter filtro = new VendaFilter();
		if(HasValue.execute(filtro)) {
			filtro.getVenda().setCliente(cliente);
		}
		
		List<Venda> vendaList = vendaService.pesquisar(filtro);
		String enderecoFormatado = "";
		if(cliente.getEndereco()!=null) {
			Endereco endereco = cliente.getEndereco();
			if(endereco.getLogradouro() != null && !endereco.getLogradouro().isEmpty()) {
				enderecoFormatado += endereco.getLogradouro()+", ";
			}
			if(endereco.getNumero() != null && !endereco.getNumero().isEmpty()) {
				enderecoFormatado += endereco.getNumero()+", ";
			}
			if(endereco.getBairro() != null && !endereco.getBairro().isEmpty()) {
				enderecoFormatado += endereco.getBairro()+", ";
			}
			if(endereco.getCep() != null && !endereco.getCep().isEmpty()) {
				enderecoFormatado += endereco.getCep()+", ";
			}
			if(endereco.getCidade() != null && !endereco.getCidade().isEmpty()) {
				enderecoFormatado += endereco.getCidade()+", ";
			}
			if(endereco.getEstado() != null) {
				enderecoFormatado += endereco.getEstado().getDescricao()+", ";
			}
			if(enderecoFormatado.length()>2) {
				enderecoFormatado = enderecoFormatado.substring(0, enderecoFormatado.length()-2);
			}
			
		}
		cliente.setEnderecoFormatado(enderecoFormatado);
		mv.addObject("filtro", filtro);
		mv.addObject("vendaList", vendaList);
		return mv;
	}
	
	@RequestMapping("/buscarVendas/{id}")
	public ModelAndView buscarVendasPorCliente(@PathVariable("id") Cliente cliente, 
			@ModelAttribute("filtro") VendaFilter filtro) {
		
		ModelAndView mv = new ModelAndView(DETALHAR_VIEW);
		if(HasValue.execute(filtro)) {
			filtro.getVenda().setCliente(cliente);
			filtro.setAvancada(true);
		}
		List<Venda> vendaList = vendaService.pesquisar(filtro);
		
		mv.addObject("cliente", cliente);
		mv.addObject("vendaList", vendaList);
		
		return mv;
		
	}
	
	@RequestMapping(value = "/detalhes", method = RequestMethod.POST)
	public ModelAndView detalhes(@RequestBody ClienteFilter clienteFilter) {
		ModelAndView mv = new ModelAndView(LISTAR_VIEW + " :: #modalDetalhes");
		Cliente cliente = clienteService.buscarPorId(clienteFilter.getCliente().getId());
		
		if(HasValue.execute(cliente)) {
			String enderecoFormatado = "";
			if(cliente.getEndereco()!=null) {
				Endereco endereco = cliente.getEndereco();
				if(endereco.getLogradouro() != null && !endereco.getLogradouro().isEmpty()) {
					enderecoFormatado += endereco.getLogradouro()+", ";
				}
				if(endereco.getNumero() != null && !endereco.getNumero().isEmpty()) {
					enderecoFormatado += endereco.getNumero()+", ";
				}
				if(endereco.getBairro() != null && !endereco.getBairro().isEmpty()) {
					enderecoFormatado += endereco.getBairro()+", ";
				}
				if(endereco.getCep() != null && !endereco.getCep().isEmpty()) {
					enderecoFormatado += endereco.getCep()+", ";
				}
				if(endereco.getCidade() != null && !endereco.getCidade().isEmpty()) {
					enderecoFormatado += endereco.getCidade()+", ";
				}
				if(endereco.getEstado() != null) {
					enderecoFormatado += endereco.getEstado().getDescricao();
				}
			}
			
			cliente.setImagemBase64(clienteService.carregarImagem(cliente.getImagemPath()));
			mv.addObject("cliente", cliente);
			mv.addObject("enderecoFormatado", enderecoFormatado);
			
			VendaFilter filtro = new VendaFilter();
			if(HasValue.execute(filtro)) {
				filtro.getVenda().setCliente(cliente);
			}
			
			List<Venda> vendaList = vendaService.pesquisar(filtro);
			mv.addObject("vendaList", vendaList);
		}
		
		return mv;
	}
	
	@ModelAttribute
	public List<StatusVenda> todosStatus(){
		return Arrays.asList(StatusVenda.values());
	}
	
	@ModelAttribute
	public List<Usuario> todosUsuarios(){
		List<Usuario> usuarioList = usuarioService.pesquisar(new UsuarioFilter());
		return usuarioList;
	}
	
}
