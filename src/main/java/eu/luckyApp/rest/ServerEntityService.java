package eu.luckyApp.rest;



import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import eu.luckyApp.modbus.exeptions.RegisterReaderReadException;
import eu.luckyApp.modbus.service.RegisterReader;
import eu.luckyApp.model.ServerEntity;
import eu.luckyApp.model.ServerRepository;


//@RestController
@RequestMapping(value="/ModbusChart/servers")
public class ServerEntityService implements Observer{
	
	@Autowired
	private ServerRepository serverRepository;
	
	@Autowired
	private RegisterReader rReader;
	
	private boolean isConnected;
	private ScheduledExecutorService scheduler;
	
//	private String returnResponse;
	//private Timer scheduler;
	
	
	@RequestMapping(method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public Iterable<ServerEntity> getServers(){
		return serverRepository.findAll();
	}
	
	
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public ServerEntity getServer(@PathVariable(value="id")Long id){
	
		return serverRepository.findOne(id);
		
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.PUT,consumes=MediaType.APPLICATION_JSON_VALUE)
	public ServerEntity updateServer(@PathVariable Long id,@RequestBody ServerEntity server){
		return serverRepository.save(server);
		
	}
	
	
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public void deleteServer(@PathVariable Long id){
		serverRepository.delete(id);
		
		
		
	}
	
	
	@RequestMapping(method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public ServerEntity updateServer(@RequestBody ServerEntity server){

		return serverRepository.save(server);
		
		
	}
	
	
	
	
/*	@RequestMapping(value="/testAdd",method=RequestMethod.GET)
	public String addServer(){
		//ServerEntity server=new ServerEntity("server one","1920182d",1024,3000,null);
		//serverRepository.save(server);
		//return server.toString();
	}*/
	
	
	@RequestMapping(value="/{id}/run",method=RequestMethod.GET)
	@ResponseBody
	public String runReadFromServer(@PathVariable("id")long id){
		ServerEntity server=serverRepository.findOne(id);
		rReader.setServerEntity(server);
		if(!isConnected){
			//scheduler=new Timer();
			rReader.addObserver(this);
			scheduler=Executors.newScheduledThreadPool(4);
			scheduler.scheduleAtFixedRate( rReader, 0, server.getTimeInterval(), TimeUnit.MILLISECONDS);
			this.isConnected=true;
			}
		return null;//returnResponse;
		
		
	}
	
	@RequestMapping(value="/{id}/stop",method=RequestMethod.GET)
	public void stopReadFrompServer(){
		scheduler.shutdown();
		this.isConnected=false;
		rReader.deleteObserver(this);
	}



	@Override
	public void update(Observable o, Object arg) {
	
			
	
		System.out.println("uwaga błąd "+((Exception)arg).getMessage());
		//returnResponse="is error";
		scheduler.shutdown();
		this.isConnected=false;
		rReader.deleteObserver(this);
		//run exception  
		throw new RegisterReaderReadException();
		
	}
	
	@ExceptionHandler(RegisterReaderReadException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public void handleResourceNotFoundException(RegisterReaderReadException ex)
	{
		System.out.println("exception run "+ex.getMessage());
	}

}
