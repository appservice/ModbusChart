package eu.luckyApp.settings;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import eu.luckyApp.rest.ExcelRS;
import eu.luckyApp.rest.ExcelRsAsStram;
import eu.luckyApp.rest.ExecutorRS;
import eu.luckyApp.rest.MeasurementRS;
import eu.luckyApp.rest.ServersService;

@Component
public class JerseyConfig extends ResourceConfig {
	
	public JerseyConfig(){
		register(ServersService.class);
		register(MeasurementRS.class);
		register(ExcelRS.class);
		//register(ExecutorRS.class);
	//	register(ExcelRsAsStram.class);
	
	}

}
