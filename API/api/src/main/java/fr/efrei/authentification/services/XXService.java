package fr.efrei.authentification.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("test")
public class XXService {

	@GET
	public String hello() {
		return "Amaury";
	}
}
