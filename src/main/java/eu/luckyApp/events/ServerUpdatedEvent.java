package eu.luckyApp.events;

import org.springframework.context.ApplicationEvent;

public class ServerUpdatedEvent<ServerEntity> extends ApplicationEvent {

	public ServerUpdatedEvent(ServerEntity source) {
		super(source);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3226340283455965479L;

}
