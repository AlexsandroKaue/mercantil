package com.kaue.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import com.kaue.dao.filter.GrupoFilter;
import com.kaue.dao.filter.UsuarioFilter;
import com.kaue.enumeration.StatusUsuario;
import com.kaue.model.Cliente;
import com.kaue.model.Endereco;
import com.kaue.model.Grupo;
import com.kaue.model.Usuario;
import com.kaue.service.GrupoService;
import com.kaue.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	private static final String CADASTRAR_VIEW = "page/usuario/Cadastrar";
	private static final String ALTERAR_VIEW = "page/usuario/Alterar";
	private static final String LISTAR_VIEW = "page/usuario/Listar";
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private GrupoService grupoService;
		
	@RequestMapping("/novo")
	public ModelAndView showFormNovo() {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		Usuario usuario = new Usuario();
		usuario.setImagemBase64(usuarioService.carregarImagem("User_"+usuario.getId()));
		mv.addObject("usuario", usuario);
		return mv;
	}
	
	@RequestMapping("{id}")
	public ModelAndView showFormEditar(@PathVariable("id") Usuario usuario) {
		ModelAndView mv = new ModelAndView(ALTERAR_VIEW);
		usuario.setImagemBase64(usuarioService.carregarImagem("User_"+usuario.getId()));
		mv.addObject("senhaAtual");
		mv.addObject("novaSenha");
		mv.addObject("novaSenhaConfirmacao");
		mv.addObject("usuario", usuario);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Usuario usuario, @RequestParam("file") MultipartFile multipartFile,
			Errors errors, RedirectAttributes attributes) {
		
		if(usuario.getId()==null && usuarioService.buscarPorLogin(usuario.getLogin())!=null) {
			errors.rejectValue("login", null, "Já existe um usuário com este login.");
		}
		
		if(errors.hasErrors()) {
			return CADASTRAR_VIEW;
		}

		String url = "";
		if(usuario.getId()==null) {
			attributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");
			url = "redirect:/usuarios/novo";
		} else {
			attributes.addFlashAttribute("mensagem", "Usuário alterado com sucesso!");
			url = "redirect:/usuarios/"+usuario.getId();
		}
		usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
		
		boolean hasFileUploaded = !multipartFile.isEmpty();
		if(hasFileUploaded) {
			try {
				usuarioService.salvarImagem(multipartFile, "User_"+usuario.getId());
			} catch (Exception e) {
				attributes.addFlashAttribute("aviso", "Não foi possível salvar a imagem");
			}
		}
		
		usuario = usuarioService.salvar(usuario);
		return url;
	}
	
	@RequestMapping(value = "/alterarSenha", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> alterarSenha(@RequestBody Map<String, String> dados) {
		Map<String, Object> response = new HashMap<String, Object>();
		
		
		String senhaAtual = dados.get("senhaAtual");
		String novaSenha = dados.get("novaSenha");
		Long id = Long.parseLong(dados.get("id"));
		Usuario usuario = usuarioService.buscarPorId(id);
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		boolean ok =  encoder.matches(senhaAtual, usuario.getSenha());
		if(ok) {
			usuario.setSenha(new BCryptPasswordEncoder().encode(novaSenha));
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
		ModelAndView mv = new ModelAndView(LISTAR_VIEW + " :: #modal-detalhes");
		usuario.setImagemBase64(usuarioService.carregarImagem("User_"+usuario.getId()));
		mv.addObject("usuario", usuario);
		
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
	
	@ModelAttribute
	public List<Grupo> todosGrupos(){
		return grupoService.pesquisar(new GrupoFilter());
	}
	
}
