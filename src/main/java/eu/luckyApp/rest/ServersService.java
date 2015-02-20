package eu.luckyApp.rest;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.luckyApp.modbus.service.RegisterReader2;
import eu.luckyApp.model.MeasurementRepository;
import eu.luckyApp.model.ServerEntity;
import eu.luckyApp.model.ServerRepository;

@Component
@Path("/servers")
public class ServersService implements Observer {

	private static final Logger LOG = Logger.getLogger(ServersService.class.getName());

	@Autowired
	private ServerRepository serverRepository;

	@Autowired
	MeasurementRepository mesasurementRepo;

	@Autowired
	private RegisterReader2 registerReader;

	private Map<Long, ScheduledExecutorService> schedulersMap = new HashMap<>();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Iterable<ServerEntity> getAll() {

		return serverRepository.findAll();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public ServerEntity getServer(@PathParam("id") long id) { 

		ServerEntity server = serverRepository.findOne(id);

		return server;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addServer(ServerEntity server, @Context UriInfo uriInfo) {

		serverRepository.save(server);
		Long serverId = server.getId();
		URI createdUri = uriInfo.getAbsolutePathBuilder().path("/" + serverId).build();

		return Response.created(createdUri).entity(server).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateServer(ServerEntity server) {
		serverRepository.save(server);
		LOG.info("zmieniono server" + server);
		return Response.noContent().build();

	}

	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteServer(ServerEntity server) {
		serverRepository.delete(server);
		return Response.noContent().build();
	}

	@DELETE
	@Path("/{id}")
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response deleteServerById(@PathParam("id") Long id) {
		serverRepository.delete(id);
		return Response.noContent().build();
	}

	@POST
	@Path("/{id}/executor")
	public Response runServer(@PathParam("id") long id) {
		ServerEntity server = serverRepository.findOne(id);
		registerReader.setServerEntity(server);
		if ((schedulersMap.get(id)) == null) {

			registerReader.addObserver(this);
			ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
			schedulersMap.put(id, scheduler);
			scheduler.scheduleAtFixedRate(registerReader, 0, server.getTimeInterval(), TimeUnit.MILLISECONDS);

			LOG.warn("Odczyt włączony. " + server.getIp() + ":" + server.getPort());

			return Response.ok().build();
		}
		return Response.serverError().build();

	}

	@DELETE
	@Path("/{id}/executor")
	public Response stopServer(@PathParam("id") Long id) {
		ServerEntity server = serverRepository.findOne(id);

		ScheduledExecutorService scheduler = schedulersMap.get(id);
		scheduler.shutdown();
		schedulersMap.remove(id);
		// registerReader.setConnected(false);
		registerReader.deleteObserver(this);
		LOG.warn("Odczyt z servera zatrzymany! " + server.getIp() + ":" + server.getPort());

		return Response.ok().build();
	}

	
	/**
	 * 
	 * @param id
	 *            -it is server id
	 * @return true if executor is scheduling task
	 */
	@GET
	@Path("/{id}/executor")
	public boolean isConntectedToServer(@PathParam("id") Long id) {
		if ((schedulersMap.get(id)) != null) {
			return true;
		}

		return false;
	}

	@Override
	public void update(Observable o, Object arg) {

		ServerEntity server = ((RegisterReader2) o).getServerEntity();
		LOG.error("Uwaga błąd połączenia/odczytu z: " + server.getIp() + ":" + server.getPort() + " |" + ((Exception) arg).getMessage());

		Long id = server.getId();

		ScheduledExecutorService scheduler = schedulersMap.get(id);

		scheduler.shutdown();
		schedulersMap.remove(id);
		// registerReader.setConnected(false);
		registerReader.deleteObserver(this);

	}

	

}
