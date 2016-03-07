package eu.luckyApp.modbus.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import eu.luckyApp.events.MeasureEvent;
import eu.luckyApp.events.RegisterReaderExceptionEvent;
import eu.luckyApp.modbus.exeptions.RegisterReaderException;
import eu.luckyApp.modbus.facade.MyModbusTCPMaster;
import eu.luckyApp.model.Measurement;
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
@Service
public class RegisterReader implements Runnable, ApplicationEventPublisherAware {

	private MyModbusTCPMaster modbusMaster;
	private static final Logger LOG = Logger.getLogger(RegisterReader.class);
	private ServerEntity serverEntity;
	private boolean connected;
	ApplicationEventPublisher applicationEventPublisher;

	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}

	// private static final double WSPOLCZYNNIK = 0.0027466659;

	public ServerEntity getServerEntity() {
		return serverEntity;
	}

	public void setServerEntity(ServerEntity serverEntity) {
		this.serverEntity = serverEntity;
	}

	public void startConnection() throws Exception {
		modbusMaster = new MyModbusTCPMaster(serverEntity.getIp(), serverEntity.getPort());

		modbusMaster.connect();
		connected = true;

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
				LOG.error(e);

				RegisterReaderExceptionEvent<RegisterReaderException> event = new RegisterReaderExceptionEvent<>(
						new RegisterReaderException("Błąd serwisu odczytu z PLC!"));
				applicationEventPublisher.publishEvent(event);
				stopConnection();

			}

		} else {

			RegisterReaderExceptionEvent<RegisterReaderException> event = new RegisterReaderExceptionEvent<>(
					new RegisterReaderException("Błąd połączenia!"));
			applicationEventPublisher.publishEvent(event);

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

		MeasureEvent<Measurement> mEvent = prepareMeasurementEvent(resultList);
		applicationEventPublisher.publishEvent(mEvent);

	}

	// ----------------------------------------------------------------
	private void readIntegerData() throws ModbusException {
		Register[] registers = modbusMaster.readMultipleRegisters(Modbus.DEFAULT_UNIT_ID,
				serverEntity.getFirstRegisterPos(), serverEntity.getSensorsName().size() + 1);

		List<Double> resultList = new ArrayList<>();

		int len = registers.length;

		for (int i = 0; i < len; i++) {
			int value = registers[i].getValue();
			resultList.add(((double) value)/* * serverEntity.getScaleFactor() */);
		}

		LOG.debug("Readed data from PLC: " + resultList);

		MeasureEvent<Measurement> mEvent = prepareMeasurementEvent(resultList);
		applicationEventPublisher.publishEvent(mEvent);

	}

	public void writeIntToRegister(int ref, int value) throws ModbusException {

		Register r = new SimpleRegister();
		r.setValue(value);
		modbusMaster.writeSingleRegister(ref, r);

	}

	public void writeFlag(int ref, boolean state) throws ModbusException {
		modbusMaster.writeCoil(0, ref, state);
	}

	public void resetFlag(int ref) throws ModbusException, InterruptedException {

		writeFlag(ref, true);
		Thread.sleep(100);
		writeFlag(ref, false);

	}

	private MeasureEvent<Measurement> prepareMeasurementEvent(List<Double> myData) {
		LOG.debug("myData: from on next" + myData);
		Measurement measurementOnline = new Measurement();
		measurementOnline.setDate(new Date());
		measurementOnline
				.setEnergyConsumption(myData.get(0) * this.getServerEntity().getScaleFactorForElectricEnergy());

		for (int i = 1; i < myData.size(); i++) {
			measurementOnline.getMeasuredValue().add(myData.get(i) * this.getServerEntity().getScaleFactor());
		}

		return new MeasureEvent<>(measurementOnline);

	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

}
