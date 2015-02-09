package eu.luckyApp.modbus.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.net.TCPMasterConnection;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import eu.luckyApp.model.ServerEntity;

@Component
public class RegisterReader extends Observable implements Runnable  {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static final Logger LOG=Logger.getLogger(RegisterReader.class.getName());
	
	
	public RegisterReader() {
	
	}


	private ServerEntity serverEntity;
	
	public void setServerEntity(ServerEntity server){
		this.serverEntity=server;
	}

	
	/* The important instances of the classes mentioned before */
	private TCPMasterConnection con = null; //the connection
	private ModbusTCPTransaction trans = null; //the transaction
	private ReadInputRegistersRequest  req = null; //the request
	private ReadInputRegistersResponse res = null; //the response

	/* Variables for storing the parameters */
	private 	InetAddress addr = null; //the slave's address
	private int port = Modbus.DEFAULT_PORT;
	private int ref = 0; //the reference; offset where to start reading from
	private int count = 0; //the number of DI's to read
	
	
	

	

	@Override
	public void  run() {
		 
		 LOG.info("Odczyt: " + dateFormat.format(new Date()));
		
	try {

			
			addr = InetAddress.getByName(serverEntity.getIpAddress());
			port=serverEntity.getPort();
			ref=serverEntity.getReadRegister();
			count=2;
			
			
			//2. Open the connection
			con = new TCPMasterConnection(addr);			
			con.setPort(port);			
			con.connect();

			//3. Prepare the request
			req = new ReadInputRegistersRequest(ref, count);

			//4. Prepare the transaction
			trans = new ModbusTCPTransaction(con);
			trans.setRequest(req);

	
			 trans.execute();
			  res = (ReadInputRegistersResponse) trans.getResponse();
			  System.out.println("Digital Inputs Status=" + (res.getRegisterValue(0)+res.getRegisterValue(1)));
			  LOG.info("RegisterStatus: "+res.getRegisterValue(0)+res.getRegisterValue(1));
			  

			con.close();
			

		} catch (UnknownHostException e){
			this.setChanged();
			this.notifyObservers(e);
			LOG.error(e);
		}
	
	catch (Exception e ) {
			this.setChanged();
			this.notifyObservers(e);
			LOG.error(e);
			//throw e;
		}
		
	}


	public void closeConnection(){
		if(con!=null&& con.isConnected())
			con.close();
	}


	public int getTimeInterval(){
		return serverEntity.getTimeInterval();
	}



	
}
