package eu.luckyApp.rest;

import java.util.Date;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import eu.luckyApp.model.MeasurementRepository;

public abstract class MeasurementGetterResolver {

	private static final Logger LOG = Logger.getLogger(MeasurementGetterResolver.class);

	@Autowired
	MeasurementRepository measurementRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllFromServer(@PathParam("serverId") Long serverId, @QueryParam("startDate") long startDate,
			@QueryParam("endDate") long endDate, @DefaultValue("0") @QueryParam("timePeriod") Long timePeriod) {

		try {

			// without parameters
			if (startDate == 0 && endDate == 0 && timePeriod == 0) {
				LOG.info("function findAllFromServer is working with parameters: beginDate: " + startDate + " endDate: " + endDate);

				return Response.ok(measurementRepository.findAllFromServer(serverId)).build();
			} else

			if (timePeriod != 0) {
				Date now = new Date();
				Date beginDate = new Date(now.getTime() - timePeriod);

				return Response.ok(measurementRepository.findAllFromServerByStartDate(serverId, beginDate)).build();

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
