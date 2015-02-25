package eu.luckyApp.settings;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import eu.luckyApp.rest.ExcelRS;
import eu.luckyApp.rest.MearuementRS;
import eu.luckyApp.rest.ServersService;

@Component
public class JerseyConfig extends ResourceConfig {
	
	public JerseyConfig(){
		register(ServersService.class);
		register(MearuementRS.class);
		register(ExcelRS.class);
	
	}

}
