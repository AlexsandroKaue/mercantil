package com.kaue.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kaue.dao.filter.GrupoFilter;
import com.kaue.enumeration.StatusTitulo;
import com.kaue.model.Categoria;
import com.kaue.model.Grupo;
import com.kaue.service.GrupoService;

@Controller
@RequestMapping("/grupos")
public class GrupoController {
	
	private static final String CADASTRAR_VIEW = "page/grupo/Cadastrar";
	private static final String LISTAR_VIEW = "page/grupo/Listar";
	
	@Autowired
	private GrupoService grupoService;
	
	@RequestMapping("/novo")
	public ModelAndView showFormNovo(Grupo grupo) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		mv.addObject("grupo", new Grupo());
		return mv;
	}
	
	@RequestMapping("{id}")
	public ModelAndView showFormEditar(@PathVariable("id") Grupo grupo) {
		ModelAndView mv = new ModelAndView(CADASTRAR_VIEW);
		/* Grupo grupo = grupoService.findById(id).orElse(null); */
		mv.addObject("grupo", grupo);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Grupo grupo, Errors errors, RedirectAttributes attributes){
		if(errors.hasErrors()) {
			return CADASTRAR_VIEW;
		}
		grupoService.salvar(grupo);
		attributes.addFlashAttribute("mensagem", "Grupo cadastrado com sucesso!");
		return "redirect:/grupos/novo";
	}
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") GrupoFilter filtro) {
		if(!filtro.isAvancada()) {
			String termo = filtro.getTermo();
			if(termo!=null) {
				filtro.getGrupo().setNome(termo);
				filtro.getGrupo().setDescricao(termo);
				try {
					Long id = Long.parseLong(termo);
					filtro.getGrupo().setId(id);
				} catch(NumberFormatException nfe) {}
			}
		}
		List<Grupo> grupoList = grupoService.pesquisar(filtro);
		ModelAndView mv = new ModelAndView(LISTAR_VIEW);
		mv.addObject("grupos", grupoList);
		return mv;
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
		grupoService.excluir(id);
		attributes.addFlashAttribute("mensagem", "Título excluído com sucesso!");
		return "redirect:/grupos";
	}

}
