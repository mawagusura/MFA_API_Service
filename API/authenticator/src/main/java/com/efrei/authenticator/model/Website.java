package com.efrei.authenticator.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "website", uniqueConstraints = { @UniqueConstraint(columnNames = {"url"})})
public class Website {

    private Long id;
    private String url;
    private Set<UserWebsite> users = new HashSet<>();
    private Set<AdminWebsite> admins = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NaturalId
    @NotBlank
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @OneToMany(mappedBy = "primarykey.website", cascade = CascadeType.ALL)
    public Set<UserWebsite> getUsers() {
        return users;
    }

    public void setUsers(Set<UserWebsite> users) {
        this.users = users;
    }

    @OneToMany(mappedBy = "primarykey.website", cascade = CascadeType.ALL)
    public Set<AdminWebsite> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<AdminWebsite> admins) {
        this.admins = admins;
    }
}
