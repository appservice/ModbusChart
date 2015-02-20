package eu.luckyApp.rest;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import eu.luckyApp.model.Measurement;
import eu.luckyApp.model.MeasurementRepository;

@Path("/measurements")
public class MearuementRS {

	@Autowired
	MeasurementRepository mesasurementRepo;
/*
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Iterable<Measurement> getAll() {
		return mesasurementRepo.findAll();

	}*/

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllByDate(@QueryParam("beginDate") Date beginDate,
			@QueryParam("endDate") Date endDate) {

		if (beginDate.compareTo(endDate) < 0) {
			return Response.ok(mesasurementRepo.findAll(beginDate, endDate))
					.build();
		} else {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Measurement getOne(@PathParam("id") Long id) {
		return mesasurementRepo.findOne(id);
	}

}
