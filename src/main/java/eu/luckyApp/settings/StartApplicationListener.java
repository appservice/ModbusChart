package eu.luckyApp.settings;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
/**
 * 
 * @author lmochel
 *This is the class which wrote text when application is started (when is initialized)
 */
@Component
public class StartApplicationListener implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

	private static final Logger LOG=Logger.getLogger(StartApplicationListener.class);
	
	
	@Override
	public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
		Date startDate=new Date(event.getTimestamp());
		LOG.warn("Aplikacja wystartowala! "+startDate);
		
		
	}
	
	

}
