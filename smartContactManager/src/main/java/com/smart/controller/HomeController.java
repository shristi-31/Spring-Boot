package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title","Home - Smart contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title","About - Smart contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("user",new User());
		model.addAttribute("title","SignUp- Smart contact Manager");
		return "signup";
	}
	
	//handler for custom login
	@RequestMapping("/signin")
	public String customLogin(Model model)
	{
		model.addAttribute("title","Login- Smart contact Manager");
		return "login";
	}
	
	//handler for registering user
	@RequestMapping(value="/do_register",method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user")User user,@RequestParam(value="agreement",defaultValue = "false")boolean agreement,Model model,HttpSession session,BindingResult result)
	{
		try
		{
			if(!agreement)
			{
				System.out.println("u've not agreed the terms and conditions");
				throw new Exception("u've not agreed the terms and conditions");
			}
			if(result.hasErrors())
			{
				model.addAttribute("user",user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			User res = userRepository.save(user);
			
			System.out.println(res);
			model.addAttribute("user",user);
			model.addAttribute("user",new User());
			session.setAttribute("message",new Message("successfully registered","alert-success"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message",new Message("something went wrong","alert-danger"));
		}
		return "signup";
	}
	
	
	
}
