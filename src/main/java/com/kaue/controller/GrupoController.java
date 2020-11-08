package com.kaue.controller;

import java.util.ArrayList;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kaue.dao.filter.GrupoFilter;
import com.kaue.dao.filter.PermissaoFilter;
import com.kaue.model.Grupo;
import com.kaue.model.GrupoPermissao;
import com.kaue.model.Permissao;
import com.kaue.service.GrupoService;
import com.kaue.service.PermissaoService;
import com.kaue.util.HasValue;

@Controller
@RequestMapping("/grupos")
public class GrupoController {
	
	private static final String CADASTRAR_VIEW = "page/grupo/Cadastrar";
	private static final String LISTAR_VIEW = "page/grupo/Listar";
	
	@Autowired
	private GrupoService grupoService;
	
	@Autowired
	private PermissaoService permissaoService;
	
	@ModelAttribute
	public void todasPermissoes(Model model){
		
		List<Permissao> permissaoList = permissaoService.pesquisar(new PermissaoFilter());
		
		List<GrupoPermissao> grupoPermissaoList = new ArrayList<GrupoPermissao>();
		if(HasValue.execute(permissaoList) && !permissaoList.isEmpty()) 
		{ 
			for(Permissao permissao : permissaoList) {
				GrupoPermissao gruPer = new GrupoPermissao();
				gruPer.setPermissao(permissao); 
				grupoPermissaoList.add(gruPer); 
			} 
		}
 
		model.addAttribute("permissoes", permissaoList);
	}
	
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
		List<GrupoPermissao> gruPerList = grupo.getGrupoPermissaoList();
		List<Permissao> permissaoList = new ArrayList<Permissao>();
		if(HasValue.execute(gruPerList)) {
			for(GrupoPermissao gruPer : gruPerList) {
				permissaoList.add(gruPer.getPermissao());
			}
		}
		grupo.setPermissaoList(permissaoList);
		mv.addObject("grupo", grupo);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Grupo grupo, Errors errors, RedirectAttributes attributes){
		if(errors.hasErrors()) {
			return CADASTRAR_VIEW;
		}
		
		List<Permissao> permissaoList = grupo.getPermissaoList();
		List<GrupoPermissao> grupoPermissaoList = new ArrayList<GrupoPermissao>();
		if(HasValue.execute(permissaoList) && !permissaoList.isEmpty()) 
		 { 
			 for(Permissao permissao : permissaoList) {
				 GrupoPermissao gruPer = new GrupoPermissao();
				 gruPer.setPermissao(permissao); 
				 gruPer.setGrupo(grupo);
				 grupoPermissaoList.add(gruPer); 
			 } 
		 }
		grupo.setGrupoPermissaoList(grupoPermissaoList);
		String word = HasValue.execute(grupo.getId()) ? "alterado" : "cadastrado";
		grupoService.salvar(grupo);
		attributes.addFlashAttribute("mensagem", "Grupo "+ word +" com sucesso!");
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
		attributes.addFlashAttribute("mensagem", "Grupo excluído com sucesso!");
		return "redirect:/grupos";
	}

}
