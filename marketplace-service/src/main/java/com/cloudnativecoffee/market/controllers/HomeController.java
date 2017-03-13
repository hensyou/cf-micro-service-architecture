/**
 * 
 */
package com.cloudnativecoffee.market.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lshannon
 *
 */
@Controller
public class HomeController {
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("message", "Monday :-(");
		return "index";
	}

}
