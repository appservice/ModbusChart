package eu.luckyApp.rest;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

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
import eu.luckyApp.model.ServerEntity;
import eu.luckyApp.repository.ServerRepository;

@Component
@Path("/servers")
// @PermitAll
public class ServersService {

	private static final Logger LOG = Logger.getLogger(ServersService.class.getName());

	@Autowired
	private ServerRepository serverRepository;

	@Autowired
	ServerExecutorRS serverExecutorRs;

	@Autowired
	private FlowMeasurementRS flowMeasurementRS;

	@Autowired
	private RegisterWriterRS registerRs;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Iterable<ServerEntity> getAll() {

		return serverRepository.findAll();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{serverId}")
	// @RolesAllowed(value = { "ROLE_ADMIN" })
	// @Secured("ROLE_ADMIN")
	public ServerEntity getServer(@PathParam("serverId") long id) {

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
	@Path("/{serverId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateServer(ServerEntity server) {

		serverRepository.save(server);
		LOG.info("zmieniono server" + server);
		return Response.noContent().build();

	}

	@DELETE
	@Path("/{serverId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteServer(ServerEntity server) {
		// mesasurementRepo.findAll();
		// mRepository.deleteAllValues();
		// mRepository.deleteAllInBatch();
		serverRepository.delete(server);
		return Response.noContent().build();
	}

	@DELETE
	@Path("/{serverId}")
	public Response deleteServerById(@PathParam("serverId") long id) {
		LOG.info(id);
		// mRepository.deleteAllValues();
		// mRepository.deleteAllInBatch();
		serverRepository.delete(id);
		return Response.noContent().build();
	}

	@Path("/{serverId}/executor")
	public ServerExecutorRS getServerExecutor() {
		LOG.warn("ServerServiceRS funciton start");
		return serverExecutorRs;
	}

	@Path("/{serverId}/flow")
	public FlowMeasurementRS getFlowMeasurementRS() {
		return flowMeasurementRS;
	}

	@Path("/{serverId}/registers")
	public RegisterWriterRS showRegisterRs() {
		return registerRs;
	}

}
