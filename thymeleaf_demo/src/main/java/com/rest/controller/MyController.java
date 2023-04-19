package com.rest.controller;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MyController {

	@RequestMapping(value="/about",method=RequestMethod.GET)
	public String about(Model m)
	{
		System.out.println("inside about handler");
		m.addAttribute("name","shrishtiii");
		return "about";
		//about.html
	}
	
	//for handling iteration
	@GetMapping("/example-loop")
	public String iterateHandler(Model m) {
		//send :by creating a list
		List<String> ls=new ArrayList<>();
		Collections.addAll(ls,"pappu","gappu","tappu");
		m.addAttribute("names",ls);
		return "iterate";
	}
	
	//handler for conditional statements
	@GetMapping("/conditional")
	public String conditionalHandler(Model m)
	{
		System.out.println("condiational handler executed");
		List<Integer> ls=new ArrayList<>();
		Collections.addAll(ls,11,22,23,4,0);
		m.addAttribute("isActive","on");
		m.addAttribute("gender","f");
		m.addAttribute("myList",ls);
		return "conditional";
	}
	
	//handler for including fragments
	@GetMapping("/services")
	public String servicesHandler(Model m)
	{
		m.addAttribute("title","i like to eat samosa");
		m.addAttribute("subtitle",LocalDateTime.now().toString());
		return "services";
	}
	
	//for new about (to check inheritance)
	@GetMapping("/newabout")
	public String newAbout()
	{
		return "aboutnew";
	}
	
	
}
