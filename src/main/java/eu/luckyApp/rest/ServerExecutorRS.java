package eu.luckyApp.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import eu.luckyApp.events.MeasureEvent;
import eu.luckyApp.modbus.service.RegisterReader;
import eu.luckyApp.model.Measurement;
import eu.luckyApp.model.ServerEntity;
import eu.luckyApp.repository.ServerRepository;

@Component
@Path("/")
public class ServerExecutorRS implements ApplicationEventPublisherAware {

	private static final Logger LOG = Logger.getLogger(ServersService.class.getName());
	private ApplicationEventPublisher publisher;

	@Autowired
	private ServerRepository serverRepository;

	@Autowired
	private RegisterReader registerReader;
	ServerEntity server = null;
	
	//private rx.Observable<List<Double>> obervable;



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

			rx.Observable.create(registerReader).subscribe(new MyObserver());
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
			//obervable.unsubscribeOn(scheduler)
			LOG.warn("Odczyt z servera zatrzymany! " + server.getIp() + ":" + server.getPort());
			return Response.ok().build();
		} else {

			return Response.serverError().entity("Połączenie nie było ustanowione!")
					.header("error", "Połączenie nie było ustanowione!").build();
		}
	}

	/*	*//**
			 * 
			 * @param id
			 *            -it is server id
			 * @return true if executor is scheduling task
			 *//*
			 * @GET
			 * 
			 * @Produces(MediaType.APPLICATION_JSON) public ServerRunningChecker
			 * isConntectedToServer(@PathParam("serverId") Long id) {
			 * 
			 * ServerRunningChecker serverRunningChecker = new
			 * ServerRunningChecker(); serverRunningChecker.setServerId(id);
			 * 
			 * if ((schedulersMap.get(id)) != null) {
			 * serverRunningChecker.setConnectedToServer(true);
			 * 
			 * } else { serverRunningChecker.setConnectedToServer(false);
			 * serverRunningChecker.setErrorMessage(this.errorMessage); } return
			 * serverRunningChecker; }
			 */
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;

	}

	private class MyObserver implements rx.Observer<List<Double>> {

		@Override
		public void onCompleted() {

			LOG.info("completed");
		}

		@Override
		public void onError(Throwable e) {
			
			LOG.info("error " + e);
			schedulersMap.clear();
			

		}

		@Override
		public void onNext(List<Double> myData) {
			LOG.debug("myData: from on next" + myData);
			Measurement measurementOnline = new Measurement();
			measurementOnline.setDate(new Date());
			measurementOnline.setEnergyConsumption(myData.get(0) * server.getScaleFactorForElectricEnergy());
			// LOG.warn(myData.get(0));

			for (int i = 1; i < myData.size(); i++) {

				measurementOnline.getMeasuredValue().add(myData.get(i) * server.getScaleFactor());
			}

			MeasureEvent<Measurement> measureEvent = new MeasureEvent<>(measurementOnline);
			publisher.publishEvent(measureEvent);

		}

	}

}
