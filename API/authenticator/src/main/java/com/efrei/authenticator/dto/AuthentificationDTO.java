package com.efrei.authenticator.dto;

import java.io.Serializable;

public class AuthentificationDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1086466257272626212L;
	
	private String login;
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	} 
}
