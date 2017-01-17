package com.knongdai.account.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mangofactory.swagger.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class ViewController {
	
	@RequestMapping({"/" , "/index" , "/home" , "/login"})
	public String loginPage(@RequestParam(value="continue-site" , required=false) String continueSite ,ModelMap model){
		model.addAttribute("continueSite",continueSite);
		return "swagger";
	}
	
	@RequestMapping({"register"})
	public String registerPage(@RequestParam(value="continue-site" , required=false) String continueSite ,ModelMap model){
		model.addAttribute("continueSite",continueSite);
		return "swagger";
	}
	

}
