package eu.luckyApp.modbus.service;

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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author LMochel
 */
@Service
public class RegisterReader implements Runnable, ApplicationEventPublisherAware {

    private MyModbusTCPMaster modbusMaster;
    private static final Logger LOG = Logger.getLogger(RegisterReader.class);
    private ServerEntity serverEntity;
    private boolean connected;
    private ApplicationEventPublisher applicationEventPublisher;

    @Value(value = "${registerreader.reset_time}")
    private int resetTime;

    @Value(value = "${registerreader.power_energy_register}")
    private int powerEnergyRegister;

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



    protected MyModbusTCPMaster createMyModbusTcpMaster(ServerEntity serverEntity) {
        return new MyModbusTCPMaster(serverEntity.getIp(), serverEntity.getPort());
    }

    public void startConnection() throws Exception {
        modbusMaster = createMyModbusTcpMaster(this.serverEntity);

        modbusMaster.connect();
        connected = true;
        //  return true;

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
                        publishMeasurementEvent(readIntegerData());
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
    private List<Double> readFloatData() throws ModbusException {

        Register[] registers = modbusMaster.readMultipleRegisters(Modbus.DEFAULT_UNIT_ID,
                serverEntity.getFirstRegisterPos(), serverEntity.getSensorsName().size() * 2);

        List<Double> resultList = new ArrayList<>();
        int energyValue = getEnergyValue();
        resultList.add((double) energyValue);

        int len = registers.length;
        for (int i = 0; i < len; i += 2) {
            byte[] tmp = new byte[4];
            System.arraycopy(registers[i + 1].toBytes(), 0, tmp, 0, 2);
            System.arraycopy(registers[i].toBytes(), 0, tmp, 2, 2);
            Float myFloatData = ModbusUtil.registersToFloat(tmp);
            Double parsedToDobuleData = Double.parseDouble(myFloatData.toString());
            resultList.add(parsedToDobuleData );

        }
        return resultList;

    }

    // ----------------------------------------------------------------
    protected List<Double> readIntegerData() throws ModbusException {
        List<Double> resultList = new ArrayList<>();
        int energyValue = getEnergyValue();
        resultList.add((double) energyValue);
        getAirConsumptionSensorsValues(resultList);
        LOG.debug("Readed data from PLC: " + resultList);
        return resultList;

    }

    private int getEnergyValue() throws ModbusException {
        Register[] energyRegister = modbusMaster.readMultipleRegisters(Modbus.DEFAULT_UNIT_ID,
                powerEnergyRegister, 2);
        return convertRegistersToInt( energyRegister[1],energyRegister[0]);
    }

    private void getAirConsumptionSensorsValues(List<Double> resultList) throws ModbusException {
        Register[] registers = modbusMaster.readMultipleRegisters(Modbus.DEFAULT_UNIT_ID,
                serverEntity.getFirstRegisterPos(), serverEntity.getSensorsName().size() + 1);
        int len = registers.length;
        for (int i = 1; i < len; i++) {
            int value = registers[i].getValue();
            resultList.add(((double) value)/* * serverEntity.getScaleFactor() */);
        }
    }


    protected void publishMeasurementEvent(List<Double> dataFromRegisters) {
        MeasureEvent<Measurement> measureEvent = prepareMeasurementEvent(dataFromRegisters);
        applicationEventPublisher.publishEvent(measureEvent);
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
        Thread.sleep(resetTime);
        writeFlag(ref, false);

    }


    public void registerResetFlag(int ref) throws ModbusException, InterruptedException {
        writeIntToRegister(ref, 65535); //write to register {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        Thread.sleep(resetTime);
        writeIntToRegister(ref, 0);//write to register {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}

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

    private int convertRegistersToInt(Register register1, Register register2) {
        return ModbusUtil.registersToInt(convertRegistersToBytesArray(register1, register2));
    }

    private byte[] convertRegistersToBytesArray(Register... registers) {
        byte[] registersToBytes = new byte[registers.length * 2];
        int i = 0;
        for (Register register : registers) {
            registersToBytes[i] = register.toBytes()[0];
            i++;
            registersToBytes[i] = register.toBytes()[1];
            i++;
        }

        return registersToBytes;
    }
}
