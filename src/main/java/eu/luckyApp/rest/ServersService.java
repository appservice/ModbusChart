package eu.luckyApp.rest;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.luckyApp.modbus.service.RegisterReader;
import eu.luckyApp.model.ServerEntity;
import eu.luckyApp.model.ServerRepository;

@Component
@Path("/servers")
public class ServersService  implements Observer{
	
	private static final Logger LOG=Logger.getLogger(ServersService.class.getName());

	@Autowired
	private ServerRepository serverRepository;

	@Autowired
	private RegisterReader registerReader;
	
	private boolean isConnected;
	private ScheduledExecutorService scheduler=Executors.newScheduledThreadPool(4);
	
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Iterable<ServerEntity> getAll() {

		return serverRepository.findAll();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public ServerEntity getServer(@PathParam("id") long id   ){ //Response
		
		
		ServerEntity server= serverRepository.findOne(id);

		return server;
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addServer(ServerEntity server){
		serverRepository.save(server);
	//	URI createdUri=UriBuilder.fromResource(resource)
	
		return Response.status(Status.CREATED).build();
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateServer(ServerEntity server){
		serverRepository.save(server);		
		return Response.noContent().build();
		
	}
	
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteServer(ServerEntity server){
		serverRepository.delete(server);
		return Response.noContent().build();
	}

	@DELETE
	@Path("/{id}")
//	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteServerById(@PathParam("id")Long id){
		serverRepository.delete(id);
		return Response.noContent().build();
	}
	
	@POST
	@Path("/{id}/executor")
	public Response runServer(@PathParam("id")long id){
		ServerEntity server=serverRepository.findOne(id);
		registerReader.setServerEntity(server);
		if(!isConnected){
			registerReader.addObserver(this);
			
			this.scheduler.scheduleAtFixedRate( registerReader, 0, server.getTimeInterval(), TimeUnit.MILLISECONDS);
			this.isConnected=true;
			LOG.warn("Odczyt włączony. "+server.getIpAddress()+":"+server.getPort());

			LOG.warn(this.isConnected);
			return Response.ok().build();
		}
		return Response.serverError().build();		
		
	}

	

	@DELETE
	@Path("/{id}/executor")
	public Response stopReadFromServer(@PathParam("id") Long id){
		ServerEntity server=serverRepository.findOne(id);

		this.scheduler.shutdown();
		this.isConnected=false;
		registerReader.deleteObserver(this);
		LOG.warn("Odczyt z servera zatrzymany! "+server.getIpAddress()+":"+server.getPort());

		return Response.ok().build();
	}
	
	
	@GET
	@Path("/{id}/executor")
	public boolean isConntectedToServer(){
		return this.isConnected;
	}
	
	

	@Override
	public void update(Observable o, Object arg) {
		
		System.out.println("Uwaga błąd: "+((Exception)arg).getMessage());
		this.scheduler.shutdown();
		this.isConnected=false;
		registerReader.deleteObserver(this);
		//throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		
	}
	
	@GET
	@Path("add")
	public String addServer(){
		ServerEntity server=new ServerEntity("server one","192.168.0.183",1024,3000,null);
		serverRepository.save(server);
		return server.toString();
	}
	
}
