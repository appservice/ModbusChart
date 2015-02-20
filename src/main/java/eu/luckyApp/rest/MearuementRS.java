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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import eu.luckyApp.model.MeasurementRepository;

@Path("/servers/{id}/measurements")
public class MearuementRS {
	private static final Logger LOG = Logger.getLogger(MearuementRS.class);

	@Autowired
	MeasurementRepository measurementRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllFromServer(@PathParam("id") Long serverId, @QueryParam("startDate") long startDate, @QueryParam("endDate") long endDate) {

		try {

			// without parameters
			if (startDate == 0 && endDate == 0) {
				LOG.info("function findAllFromServer is working with parameters: beginDate: " + startDate + " endDate: " + endDate);

				return Response.ok(measurementRepository.findAllFromServer(serverId)).build();
			} else

			// with startDate parameter
			if (startDate != 0 && endDate == 0) {
				LOG.info("function findAllFromServerByStartDate is working with parameters: beginDate: " + startDate + " endDate: " + endDate);
				return Response.ok(measurementRepository.findAllFromServerByStartDate(serverId, new Date(startDate))).build();

			} else
			// with startDate and endDate parameters
			if (startDate < endDate) {

				LOG.info("function getAllByDate is working with parameters: beginDate: " + startDate + " endDate: " + endDate);
				return Response.ok(measurementRepository.findAllFromServerByDates(serverId, new Date(startDate), new Date(endDate))).build();
			}
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
		}
		return Response.status(Status.BAD_REQUEST).build();

	}
}
