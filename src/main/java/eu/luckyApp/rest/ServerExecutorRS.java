package eu.luckyApp.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.luckyApp.modbus.service.RegisterReader;
import eu.luckyApp.model.ServerEntity;
import eu.luckyApp.repository.ServerRepository;

@Component
@Path("/")
public class ServerExecutorRS  {

	private static final Logger LOG = Logger.getLogger(ServersService.class.getName());

	@Autowired
	private ServerRepository serverRepository;

	@Autowired
	private RegisterReader registerReader;
	ServerEntity server = null;
	



	private Map<Long, ScheduledExecutorService> schedulersMap = new HashMap<>();

	@POST
	public Response runServer(@PathParam("serverId") long id) {

		this.server = serverRepository.findOne(id);
		registerReader.setServerEntity(server);

		if ((schedulersMap.get(id)) == null) {
			LOG.warn("włączony");
			try {
				registerReader.startConnection();
			} catch (Exception e) {
				LOG.error(e);
				return Response.serverError().entity("Nie można nawiązać połączenia!").header("error", e.getMessage())
						.build();
			}

			ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
			
			scheduler.scheduleAtFixedRate(registerReader, 0, server.getTimeInterval(), TimeUnit.MILLISECONDS);

			//rx.Observable.create(registerReader).subscribe(new MyObserver());
			LOG.warn("Odczyt włączony. " + server.getIp() + ":" + server.getPort());
			schedulersMap.put(id, scheduler);

			return Response.ok().build();
		}

		return Response.serverError().entity("Połączenie już ustanowione!")
				.header("error", "Połączenie już ustanowione!").build();

	}

	@DELETE
	public Response stopServer(@PathParam("serverId") Long id) {
		ServerEntity server = serverRepository.findOne(id);
		ScheduledExecutorService scheduler = schedulersMap.get(id);

		if (scheduler != null) {
			scheduler.shutdown();
			schedulersMap.remove(id);
			registerReader.stopConnection();
			LOG.warn("Odczyt z servera zatrzymany! " + server.getIp() + ":" + server.getPort());
			return Response.ok().build();
		} else {

			return Response.serverError().entity("Połączenie nie było ustanowione!")
					.header("error", "Połączenie nie było ustanowione!").build();
		}
	}



}
