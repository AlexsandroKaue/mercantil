package com.kaue.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
 
	@GetMapping("/login")
	public String login() {
		return "login"; // <<< Retorna a página de login
	}
	
	@GetMapping("/login-error")
	public String login(HttpServletRequest request, Model model) {
	
		HttpSession session = request.getSession(false); 
		String errorMessage = null;
		if (session != null) { 
			AuthenticationException ex = (AuthenticationException)
			session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION); 
			if (ex != null) { 
				errorMessage = ex.getMessage();
				model.addAttribute("errorMessage", "Usuário ou senha inválido");
			} else {
				model.addAttribute("errorMessage", "Falha ao iniciar sessão. Contate o administrador do sistema.");
			}
		}
        
        return "login";
    }
	
	@GetMapping("/acessoNaoPermitido") 
	public String acessoNaoPermitido() { 
		return "acessoNaoPermitido";
	}
	
	@ModelAttribute
	public void addAttributes(Model model){
		model.addAttribute("errorMessage","");
	}

}
