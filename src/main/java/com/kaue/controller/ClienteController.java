package com.kaue.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.kaue.dao.filter.ClienteFilter;
import com.kaue.enumeration.Estado;
import com.kaue.enumeration.StatusVenda;
import com.kaue.model.Caderneta;
import com.kaue.model.Cliente;
import com.kaue.model.Endereco;
import com.kaue.model.Venda;
import com.kaue.service.CadernetaService;
import com.kaue.service.ClienteService;
import com.kaue.service.GrupoService;
import com.kaue.util.HasValue;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

	private static final String CADASTRAR_VIEW = "page/cliente/Cadastrar";
	private static final String LISTAR_VIEW = "page/cliente/Listar";
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private CadernetaService cadernetaService;
	
	@Autowired
	private GrupoService grupoService;
	
	@ModelAttribute
	public void addAttributes(Model model){
		Cliente cliente = new Cliente();
		String enderecoFormatado = "";
		model.addAttribute("cliente", cliente);
		model.addAttribute("enderecoFormatado", enderecoFormatado);
		model.addAttribute("imagem", "/custom/img/clientes/sem-imagem_2.jpg");
	}
	
	@RequestMapping("/novo")
	public ModelAndView showFormNovo() {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		Cliente cliente = new Cliente();
		cliente.setImagemBase64(clienteService.carregarImagem(null));
		cliente.setCadernetaAberta(false);
		mv.addObject("cliente", cliente);
		return mv;
	}
	
	@RequestMapping("{id}")
	public ModelAndView showFormEditar(@PathVariable("id") Cliente cliente) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		cliente.setImagemBase64(clienteService.carregarImagem(cliente.getImagemPath()));
		mv.addObject("cliente", cliente);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Cliente cliente, @RequestParam("file") MultipartFile multipartFile,
			Errors errors, RedirectAttributes attributes) {
		String url = "";
		
		try {
			if(errors.hasErrors()) {
				return CADASTRAR_VIEW;
			}
			if(cliente.getId()==null) {
				url = "redirect:/clientes/novo";
				attributes.addFlashAttribute("mensagem", "Cliente cadastrado com sucesso!");
			} else {
				attributes.addFlashAttribute("mensagem", "Cliente alterado com sucesso!");
				url = "redirect:/clientes/"+cliente.getId();
			}
			
			boolean hasFileUploaded = !multipartFile.isEmpty();
			if(hasFileUploaded) {
				try {
					Long id;
					if(HasValue.execute(cliente.getId())) {
						id = cliente.getId();
					} else {
						id = clienteService.obterMaxId();
						id = id != null ? id : 0;
						id = id + 1;
					}
					String filename = clienteService.salvarImagem(multipartFile, "Cliente_"+id);
					cliente.setImagemPath(filename);
				} catch (Exception e) {
					attributes.addFlashAttribute("aviso", "Não foi possível salvar a imagem");
				}
			}
			
			cliente = clienteService.salvar(cliente);
			
			//Parte do método para salvar/atualizar caderneta
			/*Boolean desejaAbrirCaderneta = HasValue.execute(cliente.isCadernetaAberta());
			Caderneta caderneta = cadernetaService.buscarPorCliente(cliente.getId()); 
			
			if(desejaAbrirCaderneta) {
				if(!HasValue.execute(caderneta)) {
					caderneta = new Caderneta();
					caderneta.setCliente(cliente);
				}
				caderneta.setAberta(true);
				caderneta = cadernetaService.salvar(caderneta);
			} else {
				if(HasValue.execute(caderneta)) {
					caderneta.setAberta(false);
					caderneta = cadernetaService.salvar(caderneta);
				}
			}*/
			
			return url;
		} catch(IllegalArgumentException e) {
			errors.rejectValue("dataNascimento", null, e.getMessage());
			return CADASTRAR_VIEW;
		}
	}
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") ClienteFilter filtro) {
		if(!filtro.isAvancada()) {
			String termo = filtro.getTermo();
			if(termo!=null) {
				filtro.getCliente().setNome(termo);
				filtro.getCliente().setEmail(termo);
				try {
					Long id = Long.parseLong(termo);
					filtro.getCliente().setId(id);
				} catch(NumberFormatException nfe) {}
			}
		}
		List<Cliente> clienteList = clienteService.pesquisar(filtro);
		ModelAndView mv = new ModelAndView(LISTAR_VIEW);
		mv.addObject("clientes", clienteList);
		return mv;
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable("id") Cliente cliente, RedirectAttributes attributes) {
		try {
			
			if(HasValue.execute(cliente)) {
				if(HasValue.execute(cliente.getCadernetaAberta())) {
					throw new Exception("O cliente possui caderneta aberta.");
				} else {
					List<Venda> vendaList = cliente.getVendaList();
					if(HasValue.execute(vendaList)) {
						for(Venda venda : vendaList) {
							if(venda.getStatus()==StatusVenda.ABERTA) {
								throw new Exception("O cliente possui débitos.");
							}
						}
					}
				}
				clienteService.excluir(cliente.getId());
				attributes.addFlashAttribute("mensagem", "Cliente excluído com sucesso!");
			}
			
		} catch(Exception e) {
			attributes.addFlashAttribute("mensagem_erro", e.getMessage());
		}
		return "redirect:/clientes";
	}
	
	@RequestMapping(value = "/detalhes/{id}")
	public ModelAndView detalhes(@PathVariable("id") Cliente cliente) {
		ModelAndView mv = new ModelAndView(LISTAR_VIEW + " :: #modalDetalhes");
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
		
		return mv;
	}
	
	@ModelAttribute
	public List<Estado> todosEstados() {
		return Arrays.asList(Estado.values());
	}
	
	@ModelAttribute
	public List<Cliente> todosClientes() {
		List<Cliente> clienteList = clienteService.pesquisar(new ClienteFilter());
		return clienteList;
	}
	
	
}
