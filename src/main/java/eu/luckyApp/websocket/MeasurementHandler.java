package eu.luckyApp.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.luckyApp.repository.ServerRepository;

public class MeasurementHandler extends TextWebSocketHandler implements
		ApplicationListener<MeasureEvent> {

	private static final Logger LOG = Logger
			.getLogger(MeasurementHandler.class);

	private Set<WebSocketSession> mySessions = new HashSet<>();
	
	@Autowired
	private ServerRepository serverRepository;

	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		LOG.info("connection established with session id: " + session.getId());

		// for(int i=0;i<100;i++){
		// session.sendMessage(new
		// TextMessage("tekst wysłany automatycznie "+i));
		// }
		// this.mySession = session;
		this.mySessions.add(session);
		String jsonServerObject=convertToJsonObject(serverRepository.findOne(1L));
		session.sendMessage(new TextMessage(jsonServerObject));
		
	}

	@Override
	protected void handleTextMessage(WebSocketSession session,
			TextMessage message) throws Exception {

		// session.sendMessage(message);

		if (message.getPayload().equalsIgnoreCase("start"))
			session.sendMessage(new TextMessage("to jest test"));
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus status) throws Exception {
		LOG.info("connection  with session id: " + session.getId() + " closed");
		mySessions.remove(session);

	}

/*	static final int i = 200;

	@Scheduled(fixedDelay = i)
	public void addMessege() throws IOException {
		String returnedMessageString = convertToJsonObject(createSampleMeasurement());
		TextMessage returnedMessage = new TextMessage(returnedMessageString);
		for (WebSocketSession session : mySessions) {
			if (session != null && session.isOpen()) {

				// LOG.info(returnedMessage);
				session.sendMessage(returnedMessage);

			}
		}
	}*/

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

/*	@Bean
	private Measurement createSampleMeasurement() {
		Measurement m = new Measurement();
		Random random = new Random();
		m.setId(2L);
		m.setDate(new Date());
		m.getMeasuredValue().add(random.nextDouble() * 3 + 30);
		m.getMeasuredValue().add(random.nextDouble() * 4 + 25);
		m.getMeasuredValue().add(random.nextDouble() * 2 - 10);
		m.getMeasuredValue().add(random.nextDouble() * 2 - 25);
		m.getMeasuredValue().add(random.nextDouble() * 60 - 30);
		m.getMeasuredValue().add(random.nextDouble() * 1);
		m.getMeasuredValue().add(random.nextDouble() * 4);
		return m;
	}*/

	@Override
	public void onApplicationEvent(MeasureEvent event) {
		String returnedMessageString = convertToJsonObject(event.getSource());
		TextMessage returnedMessage = new TextMessage(returnedMessageString);
		for (WebSocketSession session : mySessions) {
			if (session != null && session.isOpen()) {

				// LOG.info(returnedMessage);
				try {
					session.sendMessage(returnedMessage);
				} catch (IOException e) {
					LOG.error(e);
					//e.printStackTrace();
				}

			}
		}
	}
}
