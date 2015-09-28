package eu.luckyApp.events;

import org.springframework.context.ApplicationEvent;

public class MeasureEvent<Measurement> extends ApplicationEvent {

	public MeasureEvent(Measurement source) {
		super(source);

	}
	
	

	/* (non-Javadoc)
	 * @see java.util.EventObject#getSource()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Measurement getSource() {
		return (Measurement)super.getSource();
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = -5276702551710168140L;

}
