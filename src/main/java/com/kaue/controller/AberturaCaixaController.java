package com.kaue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kaue.model.Caixa;
import com.kaue.model.Usuario;
import com.kaue.model.UsuarioWeb;
import com.kaue.service.CaixaService;

@Controller
@RequestMapping("/aberturaCaixa")
public class AberturaCaixaController {
	
	static final String ADMINISTRADOR_VIEW = "page/caixa/Administracao";
	
	@Autowired
	private CaixaService caixaService;
	
	@RequestMapping
	public ModelAndView showFormNovo(Caixa caixa) {
		ModelAndView mv = new ModelAndView(ADMINISTRADOR_VIEW);
				
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioLogado = ((UsuarioWeb)auth.getPrincipal()).getUsuario();
		
		caixa = new Caixa();
		caixa.setNumero(1);
		//caixa.setUsuarioAbertura(usuarioLogado);
		
		mv.addObject("caixa", caixa);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Caixa caixa, Errors errors, RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			return ADMINISTRADOR_VIEW;
		} 
		
		try {
			caixa = caixaService.salvar(caixa);
			attributes.addFlashAttribute("mensagem", "Caixa aberto com sucesso!");
			return "redirect:/aberturaCaixa";
		} catch (Exception e) {
			return ADMINISTRADOR_VIEW;
		}
	}

}
