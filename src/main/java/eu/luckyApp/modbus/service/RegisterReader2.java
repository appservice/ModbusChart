package eu.luckyApp.modbus.service;

import java.util.Date;
import java.util.Observable;

import net.wimpi.modbus.facade.ModbusTCPMaster;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.util.ModbusUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import eu.luckyApp.model.Measurement;
import eu.luckyApp.model.MeasurementRepository;
import eu.luckyApp.model.ServerEntity;


@Component
@Scope("prototype")
public class RegisterReader2 extends Observable implements Runnable {

	ModbusTCPMaster modbusMaster;
	Logger LOG=Logger.getLogger(this.getClass());
	
	//@Autowired
	ServerEntity serverEntity;

	@Autowired
	MeasurementRepository measurmentsRepo;


	public ServerEntity getServerEntity() {
		return serverEntity;
	}




	public void setServerEntity(ServerEntity serverEntity) {
		this.serverEntity = serverEntity;
	}




	@Override
	public void run() {
		modbusMaster = new ModbusTCPMaster(serverEntity.getIp(), serverEntity.getPort());
		try {
			modbusMaster.connect();
			
			
			//-----------read and save to DB float data--------------
			//LOG.warn(serverEntity.getReadedDataType());
			if(serverEntity.getReadedDataType().equals("FLOAT")){
				//LOG.warn("ZADZIALALO");
				Register[] registers=modbusMaster.readMultipleRegisters(serverEntity.getFirstRegisterPos(),
						serverEntity.getReadedDataCount()*2);
				
			      Measurement measurement=new Measurement();
	              measurement.setDate(new Date());
	              measurement.setServer(serverEntity);
	              
	           
				
				int len=registers.length;
				for(int i = 0; i<len; i+=2){
	                  byte [] tmp = new byte[4];
	                  System.arraycopy(registers[i+1].toBytes(), 0, tmp, 0, 2);
	                  System.arraycopy(registers[i].toBytes(), 0, tmp, 2, 2);
	                 // LOG.warn(ModbusUtil.registersToFloat(tmp)+" ");
	                 System.out.println(ModbusUtil.registersToFloat(tmp)+" ");
	               
	                 measurement.getMeasuredValue().add(ModbusUtil.registersToFloat(tmp));
	                 
	              }
				
				measurmentsRepo.save(measurement);
	
			
			}



		} catch (Exception e) {
		//	LOG.error(e.getMessage());
			this.setChanged();
			this.notifyObservers(e);
			//e.printStackTrace();
		}finally{
			modbusMaster.disconnect();
		}

	}

}
