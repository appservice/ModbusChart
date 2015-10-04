package eu.luckyApp.rest;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.luckyApp.modbus.service.RegisterReader;
import eu.luckyApp.model.ServerEntity;
import eu.luckyApp.repository.ServerRepository;
import net.wimpi.modbus.ModbusException;

@Component
@Path("/")
// @Consumes(MediaType.APPLICATION_JSON)
public class RegisterRS {
	@Autowired
	RegisterReader rr;

	@Autowired
	ServerRepository sr;

	@PUT
	@Path("/{id}")
	public Response updateRegister(@PathParam("serverId") Long serverId, @PathParam("id") int id, int value) {
		ServerEntity se = sr.findOne(serverId);

		rr.setServerEntity(se);
		if (!rr.isConnected()) {
			rr.startConnection();
			Response myResponse = writeData(id, value);
			rr.stopConnection();
			return myResponse;
		} else {
			return writeData(id, value);
		}

	}

	/**
	 * @param id
	 * @param value
	 * @return
	 */
	private Response writeData(int id, int value) {
		try {
			rr.writeIntToRegister(id, value);
			return Response.noContent().build();
		} catch (ModbusException e) {
			return Response.serverError().header("error", e.getMessage()).build();

		}
	}
}
