package eu.luckyApp.events;

import org.springframework.context.ApplicationEvent;


public class RegisterReaderExceptionEvent<RegisterReaderException> extends ApplicationEvent {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8858140860304655365L;

	public RegisterReaderExceptionEvent(RegisterReaderException source) {
		super(source);
	}

	

}
