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
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
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

import com.kaue.dao.filter.UsuarioFilter;
import com.kaue.enumeration.StatusUsuario;
import com.kaue.model.Usuario;
import com.kaue.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	private static final String CADASTRAR_VIEW = "page/usuario/Cadastrar";
	private static final String LISTAR_VIEW = "page/usuario/Listar";
	private static final String SEM_IMAGEM = new File("/custom/img/sem-imagem_2.jpg").getPath();
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	ResourceLoader resourceLoader;
	/*
	 * @Autowired private StorageService storageService;
	 */
	
	@RequestMapping("/novo")
	public ModelAndView showFormNovo() {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		Usuario usuario = new Usuario();
		usuario.setAtivo(StatusUsuario.SIM);
		mv.addObject("imagem", SEM_IMAGEM);
		mv.addObject("usuario", usuario);
		return mv;
	}
	
	@RequestMapping("{id}")
	public ModelAndView showFormEditar(@PathVariable("id") Usuario usuario) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		try {
			ResourceUtils.getFile("classpath:static/custom/img/user_"+usuario.getId()+".png");
			File foto = new File("/custom/img/user_"+usuario.getId()+".png");
			mv.addObject("imagem", foto.getPath());
		} catch (FileNotFoundException e) {
			mv.addObject("imagem", SEM_IMAGEM);
		}
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
		try {
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
				InputStream is = multipartFile.getInputStream();
				BufferedImage bufferedImage = null;
				bufferedImage = ImageIO.read(is);
				String classpath = ResourceUtils.getFile("classpath:static/custom/img").getAbsolutePath();
				
				File file = new File(classpath+"/user_"+usuario.getId()+".png");
				if(bufferedImage!=null) {
					ImageIO.write(bufferedImage, "png", file);
				} else {
					attributes.addFlashAttribute("aviso", "Não foi possível salvar a imagem");
				}
				System.out.println("Path image: "+file.getPath());
			}
			attributes.addFlashAttribute("mensagem", mensagem);
			return url;
		} catch(DataIntegrityViolationException e) {
			errors.rejectValue("dataDeVencimento", null, e.getMessage());
			return CADASTRAR_VIEW;
		} catch (IOException e1) {
			e1.printStackTrace();
			return CADASTRAR_VIEW;
		}
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
	
	@ModelAttribute
	public List<Usuario> todosUsuarios() {
		List<Usuario> usuarioList = usuarioService.pesquisar(new UsuarioFilter());
		return usuarioList;
	}
	
	@ModelAttribute
	public List<StatusUsuario> todosStatusUsuario(){
		return Arrays.asList(StatusUsuario.values());
	}
}
