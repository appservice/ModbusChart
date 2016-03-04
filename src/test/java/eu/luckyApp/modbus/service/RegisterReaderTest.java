package eu.luckyApp.modbus.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.luckyApp.model.ServerEntity;
import net.wimpi.modbus.ModbusException;

public class RegisterReaderTest {
	RegisterReader rr;

	@Before
	public void setUp() throws Exception {
		rr = new RegisterReader();

		ServerEntity se = new ServerEntity();
		se.setIp("127.0.0.1");
		se.setPort(502);

		rr.setServerEntity(se);
		rr.startConnection();
	}

	@After
	public void tearDown() throws Exception {
		rr.stopConnection();
	}

	@Test
	public void writeIntToRegisterTest() throws ModbusException {

		//System.out.println();
		rr.writeIntToRegister(2, 44);
	}
	
	
	@Test
	public void resetFlagTest() throws ModbusException, InterruptedException{
		rr.resetFlag(0);
		
	}

}
