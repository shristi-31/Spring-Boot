package com.rest.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginData {

	@NotBlank(message="username cannot be empty")
	@Size(min=3,max=12,message="username must be btw. 3 to 12 chars")
	private String username;
	
	@Pattern(regexp="\"^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$\"\r\n" + 
			"" , message="invalid email")
	private String email;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LoginData() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "LoginData [username=" + username + ", email=" + email + "]";
	}
	
	
	
}
