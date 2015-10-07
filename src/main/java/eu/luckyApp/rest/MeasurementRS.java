package eu.luckyApp.rest;

import java.util.Date;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
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

import eu.luckyApp.model.Measurement;
import eu.luckyApp.repository.MeasurementRepository;

//@Component
//@Path("/")//{serverId}/measurements
public class MeasurementRS {
	private static final Logger LOG = Logger.getLogger(MeasurementRS.class);

	@Autowired
	MeasurementRepository measurementRepository;
	
	@Autowired
	ExcelRS excelRs;
	
	// @Autowired
	// ServerRepository serverRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllFromServer(@PathParam("serverId") Long serverId,
			@QueryParam("startDate") long startDate,
			@QueryParam("endDate") long endDate,
			@DefaultValue("0") @QueryParam("timePeriod") Long timePeriod) {

		// ServerEntity se=serverRepository.findOne(serverId);
		// int timeInterval =se.getTimeInterval();

		try {

			// without parameters
			if (startDate == 0 && endDate == 0 && timePeriod == 0) {
				LOG.debug("function findAllFromServer is working with parameters: beginDate: "
						+ startDate + " endDate: " + endDate);

				return Response
						.ok(measurementRepository
								.findAllFromServer(/* serverId */)).build();
			} else

			if (timePeriod != 0) {
				Date now = new Date();
				Date beginDate = new Date(now.getTime() - timePeriod);
				if (timePeriod < 24 * 60 * 60 * 1000) {
					return Response.ok(
							measurementRepository.findAllFromServerByStartDate(
							/* serverId, */beginDate, 1)).build();
				} else if (timePeriod < 1000 * 60 * 60 * 24 * 7) {
					return Response.ok(
							measurementRepository.findAllFromServerByStartDate(
							/* serverId, */beginDate, 5)).build();

				} else {
					return Response.ok(
							measurementRepository.findAllFromServerByStartDate(
							/* serverId, */beginDate, 30)).build();
				}
			} else

			// with startDate parameter
			if (startDate != 0 && endDate == 0) {
				LOG.debug("function findAllFromServerByStartDate is working with parameters: beginDate: "
						+ startDate + " endDate: " + endDate);
				return Response.ok(
						measurementRepository.findAllFromServerByStartDate(
						/* serverId, */new Date(startDate), 1)).build();

			} else
			// with startDate and endDate parameters
			if (startDate < endDate) {

				LOG.debug("function getAllByDate is working with parameters: beginDate: "
						+ startDate + " endDate: " + endDate);
				if (endDate - startDate < 24 * 60 * 60 * 1000) {
					return Response.ok(
							measurementRepository.findAllFromServerByDates(
							/* serverId, */new Date(startDate), new Date(
									endDate), 1)).build();
				} else if (endDate - startDate < 24 * 60 * 60 * 1000 * 7) {
					return Response.ok(
							measurementRepository.findAllFromServerByDates(
									new Date(startDate), new Date(endDate), 5))
							.build();
				} else {
					return Response
							.ok(measurementRepository.findAllFromServerByDates(
									new Date(startDate), new Date(endDate), 30))
							.build();
				}
				// }
			}
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
		}
		return Response.status(Status.BAD_REQUEST).build();

	}

	@GET
	@Path("/{measurementId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Measurement getOne(@PathParam("measurementId") Long id) {
		return measurementRepository.findOne(id);
	}

	@GET
	@Path("/last")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLastMeasurements(@PathParam("serverId") Long serverId,
			@DefaultValue("0") @QueryParam("timePeriod") Long timePeriod) {

		Long lastId = measurementRepository
				.findLastMeasurementIdByServer(/* serverId */);
		if (lastId != null) {
			Measurement m = measurementRepository.findOne(lastId);
			LOG.debug("last measurement: " + m);
			return Response.ok(m).build();
		}
		return Response.noContent().build();
	}

	@DELETE
	@Path("/{measurementId}")
	public Response deleteOne(@PathParam("measurementId") Long id) {
		measurementRepository.delete(id);

		return Response.noContent().build();
	}

	@DELETE
	public Response deleteCollection(@PathParam("serverId") Long serverId,
			@QueryParam("startDate") Long startDate,
			@QueryParam("endDate") Long endDate) {

		if (startDate > 0 && endDate > 0) {
			Iterable<Measurement> deletedMeasurements = measurementRepository
					.findAllFromServerByDates(/* serverId, */new Date(startDate),
							new Date(endDate), 1);
			measurementRepository.delete(deletedMeasurements);
			return Response.noContent().build();
		} else
			return Response.status(Status.BAD_REQUEST).build();
	}

	@Path("/download")
	public ExcelRS returnExcelRs(){
		return excelRs;
	}
}
