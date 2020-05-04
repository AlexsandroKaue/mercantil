package com.kaue.controller;

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

import com.kaue.dao.PermissaoDAO;
import com.kaue.dao.filter.PermissaoFilter;
import com.kaue.model.Permissao;
import com.kaue.model.Titulo;
import com.kaue.service.PermissaoService;

@Controller
@RequestMapping("/permissoes")
public class PermissaoController {

	@Autowired
	private PermissaoService permissaoService;
	
	@RequestMapping(value = "/novo")
	public ModelAndView showFormNovo(@ModelAttribute("permissao") Permissao permissao) {
		ModelAndView mv = new ModelAndView("/page/permissao/Cadastrar");
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Permissao permissao, Errors errors, RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			return "/page/permissao/Cadastrar";
		}
		permissaoService.salvar(permissao);
		attributes.addFlashAttribute("mensagem", "Permissão cadastrada com Sucesso!");
		return "redirect:/permissoes/novo";
	}
	
	@RequestMapping(value = "{codigo}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		permissaoService.excluir(codigo);
		attributes.addFlashAttribute("mensagem", "Permissão excluída com Sucesso!");
		return "redirect:/permissoes";
	}
	
	@RequestMapping("{codigo}")
	public ModelAndView showFormEditar(@PathVariable("codigo") Permissao permissao) {
		ModelAndView mv = new ModelAndView("/page/permissao/Cadastrar");
		mv.addObject("permissao", permissao);
		return mv;
	}
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") PermissaoFilter filtro) {
		List<Permissao> permissaoList = permissaoService.pesquisar(filtro);
		ModelAndView mv = new ModelAndView("page/permissao/Listar");
		mv.addObject("permissoes", permissaoList);
		return mv;
	}
}
