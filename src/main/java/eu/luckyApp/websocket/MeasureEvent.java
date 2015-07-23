package eu.luckyApp.websocket;

import org.springframework.context.ApplicationEvent;

public class MeasureEvent extends ApplicationEvent {

	public MeasureEvent(Object source) {
		super(source);

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5276702551710168140L;

}
