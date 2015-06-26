package eu.luckyApp.settings;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

@Component
public class ListenerClass implements ApplicationListener<ContextStartedEvent> {

	private static final Logger LOG=Logger.getLogger(ListenerClass.class);
	
	
	@Override
	public void onApplicationEvent(ContextStartedEvent event) {
		LOG.info("started evenet started");
		
	}
	
	

}
