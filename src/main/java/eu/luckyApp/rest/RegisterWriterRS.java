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

public class RegisterWriterRS {
	@Autowired
	RegisterReader registerReader;

	@Autowired
	ServerRepository serverRepository;

	@PUT
	@Path("/{registerId}")
	public Response updateRegister(@PathParam("serverId") Long serverId, @PathParam("registerId") int registerId,
			int value) {
		ServerEntity serverEntity = serverRepository.findOne(serverId);

		registerReader.setServerEntity(serverEntity);
		if (!registerReader.isConnected()) {
			try {
				registerReader.startConnection();
			} catch (Exception e) {

				Response.serverError().header("error", e.getMessage()).build();
			}

			Response myResponse = writeData(registerId, value);
			registerReader.stopConnection();
			return myResponse;
		} else {
			return writeData(registerId, value);
		}

	}

	/**
	 * @param id
	 * @param value
	 * @return
	 */
	private Response writeData(int id, int value) {
		try {
			registerReader.writeIntToRegister(id, value);
			return Response.noContent().build();
		} catch (ModbusException e) {
			return Response.serverError().header("error", e.getMessage()).build();

		}
	}
}
