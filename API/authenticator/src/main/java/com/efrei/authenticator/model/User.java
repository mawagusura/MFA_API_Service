package com.efrei.authenticator.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "user")

public class User implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -303015165520233152L;

	@Id
    private String login;

    @NotNull
    @NotBlank
    private String password;


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
