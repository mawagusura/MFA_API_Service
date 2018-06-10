package com.efrei.authenticator.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
public class User  {


    private Long id;
    private String username;
    private String email;
    private String password;
    private Set<UserWebsite> websites = new HashSet<>();
    private Set<AdminWebsite> adminWebsites = new HashSet<>();

    public User(){};

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotBlank
    @Size( min = 6, max = 100)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() { return id;}

    public void setId(Long id) { this.id = id;}

    @NaturalId
    @NotBlank
    @Size(min = 3, max = 40)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @OneToMany(mappedBy = "primarykey.user", cascade = CascadeType.ALL)
    public Set<UserWebsite> getWebsites() {
        return websites;
    }

    public void setWebsites(Set<UserWebsite> websites) {
        this.websites = websites;
    }

    @OneToMany(mappedBy = "primarykey.user", cascade = CascadeType.ALL)
    public Set<AdminWebsite> getAdminWebsites() {
        return adminWebsites;
    }

    public void setAdminWebsites(Set<AdminWebsite> adminWebsites) {
        this.adminWebsites = adminWebsites;
    }

	public String getPincode() {
		// TODO Auto-generated method stub
		return null;
	}
}
