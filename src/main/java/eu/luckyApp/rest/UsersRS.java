package eu.luckyApp.rest;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.luckyApp.settings.security.User;
import eu.luckyApp.settings.security.UserRepository;

@Path("/users")
@Component
public class UsersRS {

	@Autowired
	UserRepository userRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getAll() {
		return userRepository.findAll();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getOne(@PathParam("id") long id) {
		return userRepository.getOne(id);

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addUser(User user, @Context UriInfo uriInfo) {
		User u = userRepository.save(user);
		long userId = u.getId();
		URI createdUri = uriInfo.getAbsolutePathBuilder().path("/" + userId).build();

		return Response.created(createdUri).entity(u).build();

	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("id") long id,User user){
		user.setId(id);
		//if not chagne password (not type to form) the password will be old
		if(user.getUserPassword()==null){
			user.setUserPassword(userRepository.getOne(id).getUserPassword());
		}
		userRepository.save(user);
		return Response.noContent().build();
	}

	//@PathParam("id")long id,
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteUser(User user){
		//user.setId(id);
	    userRepository.delete(user);
	    return Response.noContent().build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deleteUser(@PathParam("id") long id){
		//user.setId(id);
	    userRepository.delete(id);
	    return Response.noContent().build();
	}
	
	


}
