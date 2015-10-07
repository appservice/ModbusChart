package eu.luckyApp.settings.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import eu.luckyApp.rest.ExcelRS;
import eu.luckyApp.rest.FilePathRS;
import eu.luckyApp.rest.LoggedUserRS;
import eu.luckyApp.rest.MeasurementRS;
import eu.luckyApp.rest.RegisterWriterRS;
import eu.luckyApp.rest.ServerExecutorRS;
import eu.luckyApp.rest.ServersService;
import eu.luckyApp.rest.UsersRS;

@Component
public class JerseyConfig extends ResourceConfig {
	
	public JerseyConfig(){
		register(ServersService.class);
		register(ServerExecutorRS.class);
	//	register(MeasurementRS.class);
		register(FilePathRS.class);
		register(LoggedUserRS.class);
		register(RegisterWriterRS.class);
		register(UsersRS.class);

	
	}

}
