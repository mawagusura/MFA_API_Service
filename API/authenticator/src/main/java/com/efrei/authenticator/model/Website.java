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
    private User admin;

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

    @OneToMany(mappedBy = "primaryKey.website", cascade = CascadeType.ALL)
    public Set<UserWebsite> getUsers() {
        return users;
    }

    public void setUsers(Set<UserWebsite> users) {
        this.users = users;
    }

    @ManyToOne
    @JoinColumn(name = "admin_id",nullable=false)
    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }
}
