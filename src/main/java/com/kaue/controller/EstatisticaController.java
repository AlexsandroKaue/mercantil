package com.kaue.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/estatisticas")
public class EstatisticaController {
	
	private static final String ESTATISTICA_VIEW = "estatistica/charts";
	
	@RequestMapping
	public ModelAndView showEstatisticas() {
		ModelAndView mv = new ModelAndView(ESTATISTICA_VIEW);
		return mv;
	}
}
