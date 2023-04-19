package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@ModelAttribute
	public void addCommonData(Model model,Principal principal)
	{
		String username=principal.getName();
		User user=userRepository.getUserByUserName(username);
		System.out.println("user: "+user);
		model.addAttribute("user", user);
	}

	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal)
	{
		String username=principal.getName();
		User user=userRepository.getUserByUserName(username);
		//System.out.println("user: "+user);
		model.addAttribute("user", user);
		return "user_dashboard";
	}
	
	//open add form handler
	@RequestMapping("/add-contact")
	public String openAddContactForm(Model model)
	{
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact",new Contact());
		return "add_contact_form";
	}
	
	//processing add contact form
	@PostMapping("/process-contact")
	public String processContact(
			@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file, 
			Principal principal,HttpSession session) {
		
		try {
			
			
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		
	
		
		
		//processing and uploading file..
		
		if(file.isEmpty())
		{
			//if the file is empty then try our message
			System.out.println("File is empty");
			contact.setImage("default.png");
			
		}
		else {
			//file the file to folder and update the name to contact
			contact.setImage(file.getOriginalFilename());
			
			
			File saveFile=new ClassPathResource("static/images").getFile();
			
			Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			
			System.out.println("Image is uploaded");
			
		}
		
		
		user.getContacts().add(contact);
		
		contact.setUser(user);	
		
		
		
		this.userRepository.save(user);		
		
		
		System.out.println("DATA "+contact);
		
		System.out.println("Added to data base");
		
		//message success.......
		
	session.setAttribute("message", new Message("Your contact is added !! Add more..", "success") );
		
		}catch (Exception e) {		
			System.out.println("ERROR "+e.getMessage());
			e.printStackTrace();
		//message error
			session.setAttribute("message", new Message("Some went wrong !! Try again..", "danger") );
			
		}
		
		return "add_contact_form";
	}
	
	
	//show contact handler
	//per page 5 contacts
	//current page=0
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page")Integer page ,Model model,Principal principal)
	{
		String username =principal.getName();
		User user = this.userRepository.getUserByUserName(username);
		int userId = user.getId();
		Pageable pageable = PageRequest.of(page,5);
		Page<Contact> contacts = this.contactRepository.findContactsByUser(userId,pageable);
		model.addAttribute("contacts",contacts);
		model.addAttribute("title","View Contacts");
		model.addAttribute("currentpage",page);
		model.addAttribute("totalpages",contacts.getTotalPages());
		return "show-contacts";
	}
	
	@RequestMapping("/{cId}/contact")
	//showing particular contact details
	public String showContactDetail(@PathVariable("cId")Integer cId,Model model,Principal principal)
	{
		String username =principal.getName();
		User user = this.userRepository.getUserByUserName(username);
		
		System.out.println(cId);
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();
		
		if(user.getId()==contact.getUser().getId())
		{
			model.addAttribute("contact",contact);
			model.addAttribute("title",contact.getName());
		}
	
		return "contact_detail";
	}
	
	//deleting a particular contact
	@RequestMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId")Integer cId,Model model,HttpSession session)
	{
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();
		contact.setUser(null);
		this.contactRepository.delete(contact);
		session.setAttribute("message",new Message("contact deleted successfully","success"));
		return "redirect:/user/show-contacts/0";
	}
	
	//open update form handler
	@PostMapping("/update-contact/{cId}")
	public String updateForm(@PathVariable("cId")Integer cId,Model model)
	{
		model.addAttribute("title","Update contact");
		Contact contact = this.contactRepository.findById(cId).get();
		model.addAttribute("contact",contact);
		return "update-form";
	}
	
	//update contact
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file, Principal principal,HttpSession session,Model model)
	{
		
		//old contact details
		Contact oldContact = this.contactRepository.findById(contact.getcId()).get();
		
		try 
		{
			//image
			if(!file.isEmpty())
			{
				
				//delete old photo
				File deleteFile=new ClassPathResource("static/images").getFile();
				File file1=	new File(deleteFile,oldContact.getImage());
				file1.delete();
				
				
				//update new photo
				File saveFile=new ClassPathResource("static/images").getFile();
				
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
					
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				contact.setImage(file.getOriginalFilename());
			}
			else
			{
				contact.setImage(oldContact.getImage());
			}
			User user=this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			
			session.setAttribute("message",new Message("your contact is updated","success"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return "redirect:/user/"+contact.getcId()+"/contact";
	}
	
	//profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model)
	{
		model.addAttribute("title","profile page");
		return "profile";
	}
	
}
