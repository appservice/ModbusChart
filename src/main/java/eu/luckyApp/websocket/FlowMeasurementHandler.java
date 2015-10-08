package eu.luckyApp.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.luckyApp.events.MeasureEvent;
import eu.luckyApp.events.ServerUpdatedEvent;
import eu.luckyApp.modbus.service.ExcelCreator;
import eu.luckyApp.model.FilePathEntity;
import eu.luckyApp.model.Measurement;
import eu.luckyApp.model.ServerEntity;
import eu.luckyApp.repository.FilePathRepository;
import eu.luckyApp.repository.ServerRepository;
import eu.luckyApp.websocket.utils.Square;
import eu.luckyApp.websocket.utils.SquareManager;

//@Component
public class FlowMeasurementHandler extends TextWebSocketHandler implements ApplicationListener {

	private static final Logger LOG = Logger.getLogger(FlowMeasurementHandler.class);

	private Map<String, WebSocketSession> mySessions = new ConcurrentHashMap<>();

	private List<Measurement> mesList = new ArrayList<>();

	private Measurement tempMes;
	private SquareManager sm = new SquareManager();

	@Autowired
	private ServerRepository serverRepository;

	@Autowired
	private FilePathRepository filePathRepository;

	private Double dyvider;

	private boolean makeReset;

	private int numberOfValue;

	private int mCounter=0;

	private ServerEntity server;

	@PostConstruct
	public void initialize() {
		server = serverRepository.findOne(1L);
		if (server == null) {
			server=new ServerEntity();
			server.setSavedMeasurementNumber(1);
			server.setTimeInterval(2000);  //default 2 s
			
		}						
		dyvider = 3600000 / (double) (server.getTimeInterval());
		sm.setTimeInterval(server.getTimeInterval());
	}

	
	
	@Override
	public void onApplicationEvent(ApplicationEvent evt) {

		// -------------MEASUREMENT EVENT-----------------------
		if (evt instanceof MeasureEvent) {

			Measurement m = (Measurement) evt.getSource();
			prepareTemporaryMeasurement(m);

			if (mCounter % server.getSavedMeasurementNumber() == 0) {
				mesList.add(tempMes.deepClone());
				sendSingleMessage(tempMes);
				List<Square> sl = sm.calculateSquare(m);
				sendSingleMessage(sl);
				mCounter = 0;
			}
			mCounter++;
			// sm.setSquareAmount(serverRepository.findOne(1l).getSensorsName().size());


		}

		// -------------SERVER UPDATE EVENT-----------------------
		if (evt instanceof ServerUpdatedEvent) {
			
			ServerEntity s = (ServerEntity) evt.getSource();
			this.server=s;
			dyvider = 3600000 / (double) (s.getTimeInterval());
			sm.setTimeInterval(s.getTimeInterval());
			LOG.info("server updated: " + s);
		}

	}

	private void prepareTemporaryMeasurement(Measurement m) {
		if (this.tempMes == null) {
			m.clearValuesList();
			this.tempMes = calculatePerHour(m, dyvider);

		} else {

			tempMes.addAndCalculatePerHour(m, dyvider);

			if (makeReset) {
				tempMes.getMeasuredValue().set(numberOfValue, 0.0);
				makeReset = false;
			}

		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		this.mySessions.put(session.getId(), session);
		LOG.info("connection established with session id: " + session.getId());

		synchronized (session) {
			session.sendMessage(new TextMessage(convertToJsonObject(serverRepository.findOne(1L))));
			session.sendMessage(new TextMessage(convertToJsonObject(mesList)));
			// LOG.warn(this.tempMes);
		}
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		synchronized (session) {
			super.handleTextMessage(session, message);
			resetSensor(message);
		}
	}

	/**
	 * @param message
	 */
	private void resetSensor(TextMessage message) {

		String[] messageReceived = message.getPayload().split(":");

		if (messageReceived[0].equals("reset")) {
			numberOfValue = Integer.parseInt(messageReceived[1]);
			makeReset = true;
			// sendSingleMessage(tempMes);
		}
	}

	/**
	 * Function is done after connection of websocket is closed.
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

		synchronized (session) {

			mySessions.remove(session);
			LOG.warn("session is closed");
		}
	}

	/**
	 * 
	 */
	@Scheduled(cron = "${flowmeasurementhandler.cronstetment}") // // ?"
	@Async
	public void clearMeasurementList() {
		LOG.info("Created file xls " + new Date());

		if (serverRepository.findOne(1L) != null) {
			ExecutorService es = Executors.newSingleThreadExecutor();

			List<String> seriesNames = serverRepository.findOne(1L).getSensorsName();
			Future<FilePathEntity> fpe = es.submit(new ExcelCreator(mesList, seriesNames));

			mesList = new ArrayList<>();
			sm.clear();
			this.tempMes = null;
			sendSingleMessage(new DateObject(new Date()));

			try {

				filePathRepository.saveAndFlush(fpe.get());

				LOG.info("file created: " + fpe.get().getAbsolutePath());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			es.shutdown();
		}

	}

	/**
	 * 
	 * @param object
	 *            -Measurement
	 * @return String JSON representation of measurement object
	 */

	private String convertToJsonObject(Object object) {
		ObjectMapper mapper = new ObjectMapper();

		try {

			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Function is sending object by single text message to websocket clients
	 * 
	 * @param message
	 */
	@Async
	private void sendSingleMessage(Object message) {

		TextMessage returnedMessage = new TextMessage(convertToJsonObject(message));
		for (WebSocketSession session : mySessions.values()) {
			if (session != null && session.isOpen()) {
				synchronized (session) {

					try {

						session.sendMessage(returnedMessage);

					} catch (IOException e) {
						LOG.error(e);
					}
				}

			}
		}
	}

	/**
	 * Function calculate ...
	 * 
	 * @param measurement
	 * @param divisor
	 * @return devided measurement
	 */
	private Measurement calculatePerHour(Measurement measurement, Double divisor) {
		Measurement returnedMeasurement = new Measurement();
		returnedMeasurement.setDate(measurement.getDate());

		for (Double value : measurement.getMeasuredValue()) {
			returnedMeasurement.getMeasuredValue().add(value / divisor);
		}
		return returnedMeasurement;
	}

	private class DateObject {
		private Date resetDate;

		public DateObject(Date resetDate) {
			super();
			this.resetDate = resetDate;
		}

		@SuppressWarnings("unused")
		public Date getResetDate() {
			return resetDate;
		}

	}

}
