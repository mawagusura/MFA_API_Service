package com.efrei.authenticator.model;

import javax.persistence.*;

@Entity
@Table(name = "admins_websites")
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.user",
                joinColumns = @JoinColumn(name = "USER_ID")),
        @AssociationOverride(name = "primaryKey.website",
                joinColumns = @JoinColumn(name = "GROUP_ID")) })
public class AdminWebsite {

    private UserWebsitesID primaryKey = new UserWebsitesID();

    @EmbeddedId
    public UserWebsitesID getPrimaryKey(){
        return primaryKey;
    }

    @Transient
    public User getUser(){return primaryKey.getUser();}

    public void setUser(User user){ primaryKey.setUser(user);}

    @Transient
    public Website getWebsite(){ return primaryKey.getWebsite();}

    public void setWebsite(Website website){ primaryKey.setWebsite(website);}
}
