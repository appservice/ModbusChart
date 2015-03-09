package eu.luckyApp.modbus.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.util.ModbusUtil;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.luckyApp.modbus.facade.MyModbusTCPMaster;
import eu.luckyApp.model.ServerEntity;

@Component
@Scope("prototype")
public class RegisterReader2 extends Observable implements Runnable {

	private MyModbusTCPMaster modbusMaster;
	private static final Logger LOG = Logger.getLogger(RegisterReader2.class);
	private ServerEntity serverEntity;

	

	public ServerEntity getServerEntity() {
		return serverEntity;
	}

	public void setServerEntity(ServerEntity serverEntity) {
		this.serverEntity = serverEntity;
	}

	@Override
	public void run() {
		modbusMaster = new MyModbusTCPMaster(serverEntity.getIp(), serverEntity.getPort());
		try {
			modbusMaster.connect();

			// -----------read and save to DB float data--------------
			// LOG.warn(serverEntity.getReadedDataType());
			LOG.info("READED TYPE FROM MODBUS: " + new Date() + " " + serverEntity.getReadedDataType());

			if (serverEntity.getReadedDataType().equalsIgnoreCase("FLOAT")) {
				//modbusMaster.re
				
				Register[] registers = modbusMaster.readMultipleRegisters(Modbus.DEFAULT_UNIT_ID,serverEntity.getFirstRegisterPos(), serverEntity.getReadedDataCount() * 2);

				List<Double> resultList = new ArrayList<>();

				int len = registers.length;
				for (int i = 0; i < len; i += 2) {
					byte[] tmp = new byte[4];
					System.arraycopy(registers[i + 1].toBytes(), 0, tmp, 0, 2);
					System.arraycopy(registers[i].toBytes(), 0, tmp, 2, 2);
					Float myFloatData = ModbusUtil.registersToFloat(tmp);
					Double parsedToDobuleData = Double.parseDouble(myFloatData.toString());
					resultList.add(parsedToDobuleData);

				}
				//send data to observer
				this.setChanged();
				this.notifyObservers(resultList);

			}

		} catch (Exception e) {
			
			//send exception to observer
			this.setChanged();
			this.notifyObservers(e);
		} finally {
			modbusMaster.disconnect();
		}

	}

}
