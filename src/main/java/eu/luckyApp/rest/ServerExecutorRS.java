package eu.luckyApp.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
import eu.luckyApp.model.ServerRunningChecker;
import eu.luckyApp.repository.ServerRepository;

@Component
@Path("/")
public class ServerExecutorRS implements Observer, ApplicationEventPublisherAware {

	private static final Logger LOG = Logger.getLogger(ServersService.class.getName());
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private ServerRepository serverRepository;
	 

	@Autowired
	private RegisterReader registerReader;

	private String errorMessage;

	private Map<Long, ScheduledExecutorService> schedulersMap = new HashMap<>();

	@POST
	public Response runServer(@PathParam("serverId") long id) {

		ServerEntity server = serverRepository.findOne(id);
		System.out.println(server.getSensorsName());
		// registerReader=new RegisterReader();
		registerReader.setServerEntity(server);

		if ((schedulersMap.get(id)) == null) {
			registerReader.startConnection();
			registerReader.addObserver(this);

			ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

			schedulersMap.put(id, scheduler);

			scheduler.scheduleAtFixedRate(registerReader, 0, server.getTimeInterval(), TimeUnit.MILLISECONDS);

			//LOG.info("schedulersMap after add:" + schedulersMap);
			LOG.warn("Odczyt włączony. " + server.getIp() + ":" + server.getPort());

			return Response.ok().build();
		}
		return Response.serverError().build();

	}

	@DELETE
	public Response stopServer(@PathParam("serverId") Long id) {
		ServerEntity server = serverRepository.findOne(id);
		ScheduledExecutorService scheduler = schedulersMap.get(id);

		if (scheduler != null) {
			scheduler.shutdown();
			schedulersMap.remove(id);
			// registerReader.setConnected(false);
			registerReader.deleteObserver(this);
			registerReader.stopConnection();
			LOG.warn("Odczyt z servera zatrzymany! " + server.getIp() + ":" + server.getPort());
			return Response.ok().build();
		} else {

			return Response.serverError().build();
		}
	}

	/**
	 * 
	 * @param id
	 *            -it is server id
	 * @return true if executor is scheduling task
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ServerRunningChecker isConntectedToServer(@PathParam("serverId") Long id) {

		ServerRunningChecker serverRunningChecker = new ServerRunningChecker();
		serverRunningChecker.setServerId(id);

		if ((schedulersMap.get(id)) != null) {
			serverRunningChecker.setConnectedToServer(true);

		} else {
			serverRunningChecker.setConnectedToServer(false);
			serverRunningChecker.setErrorMessage(this.errorMessage);
		}
		return serverRunningChecker;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object dataObject) {
		ServerEntity server = ((RegisterReader) o).getServerEntity();

		if (dataObject instanceof List) {

			
			List<Double> myData = (List<Double>) dataObject;
			
			Measurement  measurementOnline = new Measurement();
			measurementOnline.setDate(new Date());
			measurementOnline.setEnergyConsumption(myData.get(0)*server.getScaleFactorForElectricEnergy());
			//LOG.warn(myData.get(0));

			for(int i=1;i<myData.size();i++){
				
				measurementOnline.getMeasuredValue().add(myData.get(i)*server.getScaleFactor());
			}

			MeasureEvent<Measurement> measureEvent = new MeasureEvent<>(measurementOnline);
			publisher.publishEvent(measureEvent);

			this.errorMessage = "";

		}

		if (dataObject instanceof Exception) {
			Exception ex = (Exception) dataObject;
			LOG.error("Error Uwaga błąd połączenia/odczytu z: " + server.getIp() + ":" + server.getPort() + " |"
					+ ex.getMessage());
			this.errorMessage = ex.getMessage();
			Long id = server.getId();

			ScheduledExecutorService scheduler = schedulersMap.get(id);

			scheduler.shutdown();
			schedulersMap.remove(id);
			registerReader.deleteObserver(this);
		}

	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher=publisher;
		
	}
	

}
