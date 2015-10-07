package eu.luckyApp.modbus.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import eu.luckyApp.modbus.facade.MyModbusTCPMaster;
import eu.luckyApp.model.ServerEntity;
import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleRegister;
import net.wimpi.modbus.util.ModbusUtil;

/**
 * @author LMochel
 *
 */
@Component
// @Scope("prototype")
public class RegisterReader extends Observable implements Runnable {

	private MyModbusTCPMaster modbusMaster;
	private static final Logger LOG = Logger.getLogger(RegisterReader.class);
	private ServerEntity serverEntity;
	private boolean connected;

	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}

	private static final double WSPOLCZYNNIK = 0.0027466659;

	public ServerEntity getServerEntity() {
		return serverEntity;
	}

	public void setServerEntity(ServerEntity serverEntity) {
		this.serverEntity = serverEntity;
	}

	public void startConnection() {
		modbusMaster = new MyModbusTCPMaster(serverEntity.getIp(), serverEntity.getPort());
		try {
			modbusMaster.connect();
			connected = true;
		} catch (Exception e) {

			// send exception to observer
			LOG.error(e.getMessage());
			this.setChanged();
			this.notifyObservers(e);
		}

	}

	public void stopConnection() {
		if (connected)
			modbusMaster.disconnect();
		connected = false;
	}

	@Override
	public void run() {

		if (connected) {

			// -----------read and save to DB float data--------------
			// LOG.warn(serverEntity.getReadedDataType());
			LOG.debug("READED TYPE FROM MODBUS: " + new Date() + " " + serverEntity.getReadedDataType());

			try {

				switch (serverEntity.getReadedDataType().toUpperCase()) {
				case "FLOAT":
					readFloatData();
					break;
				case "INTEGER":
					readIntegerData();
					break;
				default:
					break;

				}

			} catch (Exception e) {

				// send exception to observer

				e.printStackTrace();
				LOG.error(e.getMessage() + " error in run function");
				stopConnection();
				this.setChanged();
				this.notifyObservers(e);

			}

		}else{
			this.setChanged();
			this.notifyObservers(new Exception("Błąd połączenia!"));
		}

	}

	// -----------------------------------------E--------------------
	private void readFloatData() throws ModbusException {

		Register[] registers = modbusMaster.readMultipleRegisters(Modbus.DEFAULT_UNIT_ID,
				serverEntity.getFirstRegisterPos(), serverEntity.getSensorsName().size() * 2);

		List<Double> resultList = new ArrayList<>();

		int len = registers.length;
		for (int i = 0; i < len; i += 2) {
			byte[] tmp = new byte[4];
			System.arraycopy(registers[i + 1].toBytes(), 0, tmp, 0, 2);
			System.arraycopy(registers[i].toBytes(), 0, tmp, 2, 2);
			Float myFloatData = ModbusUtil.registersToFloat(tmp);
			Double parsedToDobuleData = Double.parseDouble(myFloatData.toString());
			resultList.add(parsedToDobuleData * serverEntity.getScaleFactor());

		}
		// send data to observer
		this.setChanged();
		this.notifyObservers(resultList);
	}

	// ----------------------------------------------------------------
	private void readIntegerData() throws ModbusException {
		Register[] registers = modbusMaster.readMultipleRegisters(Modbus.DEFAULT_UNIT_ID,
				serverEntity.getFirstRegisterPos(), serverEntity.getSensorsName().size());

		List<Double> resultList = new ArrayList<>();

		int len = registers.length;

		for (int i = 0; i < len; i++) {

			int value = registers[i].getValue();

			resultList.add(((double) value) * serverEntity.getScaleFactor());
		}
		// send data to observer
		this.setChanged();
		this.notifyObservers(resultList);

	}

	public void writeIntToRegister(int ref, int value) throws ModbusException {
	
		Register r = new SimpleRegister();
		r.setValue(value);
		modbusMaster.writeSingleRegister(ref, r);

	
	}

}
