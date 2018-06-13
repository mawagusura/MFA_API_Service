package com.efrei.authenticator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users_websites")
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.user",
                joinColumns = @JoinColumn(name = "USER_ID")),
        @AssociationOverride(name = "primaryKey.website",
                joinColumns = @JoinColumn(name = "WEBSITE_ID")) })
public class UserWebsite implements Serializable {


    private UserWebsitesID primaryKey = new UserWebsitesID();


    private boolean waiting = false;

    @EmbeddedId
    public UserWebsitesID getPrimaryKey(){
        return primaryKey;
    }

    public void setPrimaryKey(UserWebsitesID id){this.primaryKey = id;}

    @Transient
    public User getUser(){return primaryKey.getUser();}

    public void setUser(User user){ primaryKey.setUser(user);}

    @Transient
    public Website getWebsite(){ return primaryKey.getWebsite();}

    public void setWebsite(Website website){ primaryKey.setWebsite(website);}

    @Column(name = "waiting")
    @Value("${users_websites.waiting:false}")
    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    @Override
    public String toString(){
        return this.getWebsite().getUrl();
    }
}
