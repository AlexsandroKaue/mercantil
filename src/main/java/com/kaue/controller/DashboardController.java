package com.kaue.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/inicio")
public class DashboardController {
	
	private static final String INICIO_VIEW = "inicio";
	
	@RequestMapping
	public ModelAndView showDashboard() {
		ModelAndView mv = new ModelAndView(INICIO_VIEW);
		return mv;
	}
	
}
