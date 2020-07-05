package com.kaue.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kaue.dao.filter.ClienteFilter;
import com.kaue.enumeration.Estado;
import com.kaue.model.Cliente;
import com.kaue.model.Endereco;
import com.kaue.service.ClienteService;
import com.kaue.service.GrupoService;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

	private static final String CADASTRAR_VIEW = "page/cliente/Cadastrar";
	private static final String LISTAR_VIEW = "page/cliente/Listar";
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private GrupoService grupoService;
	
	@ModelAttribute
	public void addAttributes(Model model){
		
	}
	
	@RequestMapping("/novo")
	public ModelAndView showFormNovo() {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		Cliente cliente = new Cliente();
		mv.addObject("cliente", cliente);
		return mv;
	}
	
	@RequestMapping("{id}")
	public ModelAndView showFormEditar(@PathVariable("id") Cliente cliente) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		mv.addObject("cliente", cliente);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Cliente cliente, Errors errors, RedirectAttributes attributes) {
		String mensagem = "";
		String url = "";
				
		if(errors.hasErrors()) {
			return CADASTRAR_VIEW;
		}
		if(cliente.getId()==null) {
			mensagem = "Cliente cadastrado com sucesso!";
			url = "redirect:/clientes/novo";
		} else {
			mensagem = "Cliente alterado com sucesso!";
			url = "redirect:/clientes/"+cliente.getId();
		}
		try {
			cliente = clienteService.salvar(cliente);
			attributes.addFlashAttribute("mensagem", mensagem);
			return url;
		} catch(IllegalArgumentException e) {
			errors.rejectValue("dataVencimento", null, e.getMessage());
			return CADASTRAR_VIEW;
		}
	}
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") ClienteFilter filtro) {
		String termo = filtro.getTermo();
		if(termo!=null) {
			filtro.getCliente().setNome(termo);
			filtro.getCliente().setEmail(termo);
			try {
				Long id = Long.parseLong(termo);
				filtro.getCliente().setId(id);
			} catch(NumberFormatException nfe) {}
		}
		List<Cliente> clienteList = clienteService.pesquisar(filtro);
		ModelAndView mv = new ModelAndView(LISTAR_VIEW);
		mv.addObject("clientes", clienteList);
		return mv;
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
		clienteService.excluir(id);
		attributes.addFlashAttribute("mensagem", "Cliente excluído com sucesso!");
		return "redirect:/clientes";
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
	
	private File buscarFotoDoCliente(String name) {
		File file = null;
		try {
			ResourceUtils.getFile("classpath:static/custom/img/users/"+name);
			file = new File("/custom/img/users/"+name);
		} catch (FileNotFoundException e) {}
		
		return file;
	}
	
	private boolean salvarFotoDoCliente(InputStream is, String name) {
		try {
			BufferedImage bufferedImage = ImageIO.read(is);
			if(bufferedImage!=null) {
				File file = ResourceUtils.getFile("classpath:static/custom/img/users");
				file = new File(file.getAbsolutePath()+"/"+name);
				ImageIO.write(bufferedImage, "png", file);
			} else {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	
}