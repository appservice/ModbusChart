package eu.luckyApp.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;



@Component
@Path("/loggedUser")
@Produces(value=MediaType.APPLICATION_JSON)
public class LoggedUserRS {

	@GET
	@Path("/{id}")
	public Response getCurrentUserName(){
		
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		ReturnedUser myUser=new ReturnedUser();
		myUser.setName(auth.getName());
		
		Collection<String> myRole=new ArrayList<>(auth.getAuthorities().size());
		myRole.addAll(auth.getAuthorities().stream().map((Function<GrantedAuthority, String>) GrantedAuthority::getAuthority).collect(Collectors.toList()));
		myUser.setRoles(myRole);
       

		return Response.ok(myUser).build();
	}

@XmlRootElement
 private class ReturnedUser{
	 String name;
	 Collection<String> roles;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the roles
	 */
	public Collection<String> getRoles() {
		return roles;
	}
	/**
	 * @param roles the roles to set
	 */
	public void setRoles(Collection<String> roles) {
		this.roles = roles;
	}
	 
 }
}
