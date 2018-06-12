package com.efrei.authenticator.dto;

import javax.validation.constraints.NotBlank;

public class ValidateTokensDTO {

	@NotBlank
	private String userToken;
	
	@NotBlank
	private String mobileToken;

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getMobileToken() {
		return mobileToken;
	}

	public void setMobileToken(String mobileToken) {
		this.mobileToken = mobileToken;
	}
	
}
