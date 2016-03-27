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

import eu.luckyApp.events.MeasureEvent;
import eu.luckyApp.model.Measurement;
import eu.luckyApp.repository.ServerRepository;

public class MeasurementHandler extends TextWebSocketHandler implements ApplicationListener<MeasureEvent> {

	private static final Logger LOG = Logger.getLogger(MeasurementHandler.class);

	private Set<WebSocketSession> mySessions = new HashSet<>();
	// public static final int QUEUE_SIZE=60;
	
	//CircularFifoQueue<Measurement> queue=new CircularFifoQueue<>(QUEUE_SIZE);

	@Autowired
	private ServerRepository serverRepository;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		LOG.debug("connection established with session id: " + session.getId());

		this.mySessions.add(session);

		String jsonServerObject = convertToJsonObject(serverRepository.findOne(1L));
		synchronized (session) {
			session.sendMessage(new TextMessage(jsonServerObject));
			//session.sendMessage(new TextMessage(convertToJsonObject(queue)));
		}
	}



	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		LOG.debug("connection  with session id: " + session.getId() + " closed");
		mySessions.remove(session);

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

	@Override
	public void onApplicationEvent(MeasureEvent event) {
		Measurement m=(Measurement) event.getSource();
		// queue.add(m);		
		sendSingleMessage(m);
	}
	
	
	
	private void sendSingleMessage(Object message) {

		TextMessage returnedMessage = new TextMessage(convertToJsonObject(message));
		mySessions.stream().filter(session -> session != null && session.isOpen()).forEach(session -> {
			synchronized (session) {

				try {

					session.sendMessage(returnedMessage);

				} catch (IOException e) {
					LOG.error(e);
				}
			}

		});
	}
}
