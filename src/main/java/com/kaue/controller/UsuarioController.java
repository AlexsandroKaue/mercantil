package com.kaue.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kaue.dao.filter.UsuarioFilter;
import com.kaue.enumeration.StatusUsuario;
import com.kaue.model.Produto;
import com.kaue.model.Usuario;
import com.kaue.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	private static final String CADASTRAR_VIEW = "page/usuario/Cadastrar";
	private static final String ALTERAR_VIEW = "page/usuario/Alterar";
	private static final String LISTAR_VIEW = "page/usuario/Listar";
	
	@Autowired
	private UsuarioService usuarioService;
	
	@ModelAttribute
	public void addAttributes(Model model){
		model.addAttribute("usuario", new Usuario());
		model.addAttribute("imagem", "/custom/img/users/sem-imagem_2.jpg");
	}
	
	@RequestMapping("/novo")
	public ModelAndView showFormNovo() {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		return mv;
	}
	
	@RequestMapping("{id}")
	public ModelAndView showFormEditar(@PathVariable("id") Usuario usuario) {
		ModelAndView mv = new ModelAndView(ALTERAR_VIEW);
		
		File foto = buscarFotoDoUsuario("user_"+usuario.getId());
		if(foto!=null) {
			mv.addObject("imagem", foto.getPath());
		}

		mv.addObject("senhaAtual");
		mv.addObject("novaSenha");
		mv.addObject("novaSenhaConfirmacao");
		mv.addObject("usuario", usuario);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Usuario usuario, @RequestParam("file") MultipartFile multipartFile,
			Errors errors, RedirectAttributes attributes) {
		String mensagem = "";
		if(errors.hasErrors()) {
			return CADASTRAR_VIEW;
		}

		String url = "";
		if(usuario.getId()==null) {
			mensagem = "Usuario cadastrado com sucesso!";
			url = "redirect:/usuarios/novo";
		} else {
			mensagem = "Usuario alterado com sucesso!";
			url = "redirect:/usuarios/"+usuario.getId();
		}
		
		usuario = usuarioService.salvar(usuario);
		
		boolean hasFileUploaded = !multipartFile.isEmpty();
		if(hasFileUploaded) {
			boolean ok = false;
			InputStream is = null;
			try {
				is = multipartFile.getInputStream();
				ok = salvarFotoDoUsuario(is, "user_"+usuario.getId());
			} catch (IOException e) {}
			
			if(!ok) {
				attributes.addFlashAttribute("aviso", "Não foi possível salvar a imagem");
			}
		}
		attributes.addFlashAttribute("mensagem", mensagem);
		return url;
	}
	
	@RequestMapping(value = "/alterarSenha", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> alterarSenha(@RequestBody Map<String, String> dados) {
		Map<String, Object> response = new HashMap<String, Object>();
		
		String senhaAtual = dados.get("senhaAtual");
		String novaSenha = dados.get("novaSenha");
		Long id = Long.parseLong(dados.get("id"));
		Usuario usuario = usuarioService.buscarPorId(id);
		boolean ok = usuario.getSenha().equals(senhaAtual);
		if(ok) {
			usuario.setSenha(novaSenha);
			usuario = usuarioService.salvar(usuario);
			response.put("ok", true);
			response.put("mensagem", "Senha atualizada com sucesso");
		} else {
			response.put("ok", false);
			response.put("mensagem", "A senha informada não combina com a atual");
		}
		
		return response;
	}
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") UsuarioFilter filtro) {
		String termo = filtro.getTermo();
		if(termo!=null) {
			filtro.getUsuario().setNome(termo);
			filtro.getUsuario().setEmail(termo);
			try {
				Long id = Long.parseLong(termo);
				filtro.getUsuario().setId(id);
			} catch(NumberFormatException nfe) {}
		}
		List<Usuario> usuarioList = usuarioService.pesquisar(filtro);
		ModelAndView mv = new ModelAndView(LISTAR_VIEW);
		mv.addObject("usuarios", usuarioList);
		return mv;
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
		usuarioService.excluir(id);
		attributes.addFlashAttribute("mensagem", "Usuario excluído com sucesso!");
		return "redirect:/usuarios";
	}
	
	@RequestMapping(value = "/detalhes/{id}")
	public ModelAndView detalhes(@PathVariable("id") Usuario usuario) {
		ModelAndView mv = new ModelAndView(LISTAR_VIEW + " :: #modalDetalhesUsuario");
		mv.addObject("usuario", usuario);
		File foto = buscarFotoDoUsuario("user_"+usuario.getId());
		if(foto!=null) {
			mv.addObject("imagem", foto.getPath());
		}
		return mv;
	}
	
	@ModelAttribute
	public List<Usuario> todosUsuarios() {
		List<Usuario> usuarioList = usuarioService.pesquisar(new UsuarioFilter());
		return usuarioList;
	}
	
	@ModelAttribute
	public List<StatusUsuario> todosStatusUsuario(){
		return Arrays.asList(StatusUsuario.values());
	}
	
	private File buscarFotoDoUsuario(String name) {
		File file = null;
		try {
			ResourceUtils.getFile("classpath:static/custom/img/users/"+name);
			file = new File("/custom/img/users/"+name);
		} catch (FileNotFoundException e) {}
		
		return file;
	}
	
	private boolean salvarFotoDoUsuario(InputStream is, String name) {
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
