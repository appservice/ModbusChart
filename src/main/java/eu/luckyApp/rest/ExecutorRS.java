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
import org.springframework.stereotype.Component;

import eu.luckyApp.modbus.service.RegisterReader;
import eu.luckyApp.model.Measurement;
import eu.luckyApp.model.ServerEntity;
import eu.luckyApp.model.ServerRunningChecker;
import eu.luckyApp.repository.MeasurementRepository;
import eu.luckyApp.repository.ServerRepository;

//@Component
//@Path("/")
public class ExecutorRS implements Observer {
	
	private static final Logger LOG = Logger.getLogger(ServersService.class.getName());

	@Autowired
	private ServerRepository serverRepository;
	

	@Autowired
	MeasurementRepository mesasurementRepo;
	
	@Autowired
	private RegisterReader registerReader;

	private String errorMessage;

	private Map<Long, ScheduledExecutorService> schedulersMap = new HashMap<>();
	
	
	@POST	
	public Response runServer(@PathParam("id") long id) {
		LOG.info("test");

		ServerEntity server = serverRepository.findOne(id);
		registerReader.setServerEntity(server);
		LOG.info("schedulersMap:"+this.schedulersMap);
		if ((this.schedulersMap.get(id)) == null) {
			
			//save start measurement  with null values
			Measurement startMeasurement=new Measurement();
			//startMeasurement.setServer(server);
			startMeasurement.setDate(new Date());
			mesasurementRepo.save(startMeasurement);
			
			ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
			//TaskScheduler sched=new ThreadPoolTaskScheduler();
			//sched.schedule(registerReader, new CronTrigger(""));
			this.schedulersMap.put(id, scheduler);
			registerReader.addObserver(this);

			scheduler.scheduleAtFixedRate(registerReader, 0, server.getTimeInterval(), TimeUnit.MILLISECONDS);
			LOG.info("schedulersMap:"+this.schedulersMap);
			LOG.warn("Odczyt włączony. " + server.getIp() + ":" + server.getPort());

			return Response.ok().build();
		}
		return Response.serverError().build();

	}

	@DELETE
	public Response stopServer(@PathParam("id") Long id) {
		ServerEntity server = serverRepository.findOne(id);
		LOG.info(this.schedulersMap);
		ScheduledExecutorService scheduler = schedulersMap.get(id);
		LOG.info(this.schedulersMap.get(id));
		if (scheduler != null) {
			scheduler.shutdown();
			this.schedulersMap.remove(id);
			// registerReader.setConnected(false);
			registerReader.deleteObserver(this);
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
	public ServerRunningChecker isConntectedToServer(@PathParam("id") Long id) {

		ServerRunningChecker serverRunningChecker = new ServerRunningChecker();
		serverRunningChecker.setServerId(id);
		LOG.info("scheduler in get"+this.schedulersMap);

		if ((this.schedulersMap.get(id)) != null) {
			serverRunningChecker.setConnectedToServer(true);

		} else {
			serverRunningChecker.setConnectedToServer(false);
			serverRunningChecker.setErrorMessage(this.errorMessage);
		}
		return serverRunningChecker;
	}
	
	@Override
	public void update(Observable o, Object dataObject) {
		ServerEntity server = ((RegisterReader) o).getServerEntity();

		if (dataObject instanceof List) {
			List<Double> myData = (List<Double>) dataObject;
			Measurement measurement = new Measurement();
			measurement.setDate(new Date());
			//measurement.setServer(server);
			
			measurement.getMeasuredValue().addAll(myData);
			//server.getMeasurements().add(measurement);
			// mesasurementRepo.
			Measurement m = mesasurementRepo.save(measurement);
			LOG.info("dodano: " + m);
			LOG.info("sched:"+this.schedulersMap);
			this.errorMessage = "";
			

		}

		if (dataObject instanceof Exception) {
			Exception ex = (Exception) dataObject;
			LOG.error("Uwaga błąd połączenia/odczytu z: " + server.getIp() + ":" + server.getPort() + " |" + ex.getMessage());
			this.errorMessage = ex.getMessage();
			Long id = server.getId();

			ScheduledExecutorService scheduler = schedulersMap.get(id);

			scheduler.shutdown();
			this.schedulersMap.remove(id);
			// registerReader.setConnected(false);
			registerReader.deleteObserver(this);
		}

	}
}
