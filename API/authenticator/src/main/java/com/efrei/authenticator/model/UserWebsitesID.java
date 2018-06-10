package com.efrei.authenticator.model;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class UserWebsitesID implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6733278461627742372L;
	private User user;
    private Website website;

    @ManyToOne(cascade = CascadeType.ALL)

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }
}
