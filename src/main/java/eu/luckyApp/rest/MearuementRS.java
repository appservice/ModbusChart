package eu.luckyApp.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import eu.luckyApp.model.MeasurementRepository;

@Path("/servers/{serverId}/measurements")
public class MearuementRS {
	private static final Logger LOG = Logger.getLogger(MearuementRS.class);

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

				return Response.ok(measurementRepository.findAllFromServer(/*serverId*/)).build();
			} else

			if (timePeriod != 0) {
				Date now = new Date();
				Date beginDate = new Date(now.getTime() - timePeriod);

				return Response.ok(measurementRepository.findAllFromServerByStartDate(/*serverId, */beginDate)).build();

			} else

			// with startDate parameter
			if (startDate != 0 && endDate == 0) {
				LOG.info("function findAllFromServerByStartDate is working with parameters: beginDate: " + startDate + " endDate: " + endDate);
				return Response.ok(measurementRepository.findAllFromServerByStartDate(/*serverId,*/ new Date(startDate))).build();

			} else
			// with startDate and endDate parameters
			if (startDate < endDate) {

				LOG.info("function getAllByDate is working with parameters: beginDate: " + startDate + " endDate: " + endDate);
				return Response.ok(measurementRepository.findAllFromServerByDates(/*serverId, */new Date(startDate), new Date(endDate))).build();
			}
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
		}
		return Response.status(Status.BAD_REQUEST).build();

	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Measurement getOne(@PathParam("id") Long id) {
		return measurementRepository.findOne(id);
	}

	@GET
	@Path("/last")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLastMeasurements(@PathParam("serverId") Long serverId, @DefaultValue("0") @QueryParam("timePeriod") Long timePeriod) {

		Long lastId = measurementRepository.findLastMeasurementIdByServer(/*serverId*/);
		if (lastId != null) {
			Measurement m = measurementRepository.findOne(lastId);
			LOG.info("last measurement: " + m);
			return Response.ok(m).build();
		}
		return Response.noContent().build();
	}

	
	@GET
	@Path("/by-hours")
	@Produces(MediaType.APPLICATION_JSON)
	public Iterable<Measurement> getAllInEveryHour(@PathParam("serverId") Long serverId) {
		LOG.warn("test");
		return measurementRepository.findAllInEveryHour(/*serverId*/);
	}

	
	@DELETE
	@Path("/{id}")
	public Response deleteOne(@PathParam("id") Long id) {
		measurementRepository.delete(id);

		return Response.noContent().build();
	}

	@DELETE
	public Response deleteCollection(@PathParam("serverId") Long serverId, @QueryParam("startDate") Long startDate,
			@QueryParam("endDate") Long endDate) {

		if (startDate > 0 && endDate > 0) {
			Iterable<Measurement> deletedMeasurements = measurementRepository.findAllFromServerByDates(/*serverId,*/ new Date(startDate), new Date(
					endDate));
			measurementRepository.delete(deletedMeasurements);
			return Response.noContent().build();
		} else
			return Response.status(Status.BAD_REQUEST).build();
	}


	
	
	private Iterable<Measurement> getCollectionByAverage(int rowNumbers, Iterable<Measurement> fullList) {

		List<Measurement> response = new ArrayList<Measurement>();
		double avgValue;
		int i = 0;
		for (Measurement m : fullList) {

			avgValue = +m.getMeasuredValue().get(0);
			if (i == rowNumbers) {
				long j = 1;
				Measurement myMeasuremnt = new Measurement();
				myMeasuremnt.setDate(m.getDate());
				myMeasuremnt.setId(j);

				// myMeasuremnt.setMeasuredValue(new List<>);

				j++;
				//myMeasuremnt.setServer(m.getServer());
				response.add(myMeasuremnt);
			}
			i++;

			// for(int i=0;i<m.getMeasuredValue().size()-1;i++)
			// avgValue=m.getMeasuredValue().get(i);

		}
		return response;

	}
}
