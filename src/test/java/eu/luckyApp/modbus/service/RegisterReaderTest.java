package eu.luckyApp.modbus.service;

import eu.luckyApp.modbus.facade.MyModbusTCPMaster;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleRegister;
import org.junit.*;

import eu.luckyApp.model.ServerEntity;
import net.wimpi.modbus.ModbusException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mock.*;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class RegisterReaderTest {

    @Mock
    MyModbusTCPMaster tcpMaster;// = new MyModbusTCPMaster("localhost", 502);


    @InjectMocks
    RegisterReader rr;

    @Mock
    ServerEntity se;
    @Mock
    ApplicationEventPublisher aep;

    public RegisterReaderTest() {
        MockitoAnnotations.initMocks(this);
    }


    @Before
    public void setUp() throws Exception {


        doNothing().when(tcpMaster).connect();
        List<String> sensrosName = new ArrayList<>();
        sensrosName.add("sensor 1");
        sensrosName.add("sensror 2");
        when(se.getSensorsName()).thenReturn(sensrosName);
        when(se.getFirstRegisterPos()).thenReturn(0);

        rr = new RegisterReader() {
            @Override
            protected MyModbusTCPMaster createMyModbusTcpMaster(ServerEntity serverEntity) {
                return tcpMaster;
            }
        };
        rr.setServerEntity(se);
        rr.setApplicationEventPublisher(aep);

    }

    @After
    public void tearDown() throws Exception {
        // rr.stopConnection();
    }

    @Test
    // @Ignore
    public void testConnection() throws Exception {
        rr.startConnection();
        verify(tcpMaster, times(1)).connect();


    }


    @Test
    @Ignore
    public void writeIntToRegisterTest() throws Exception {

        //  rr.writeIntToRegister(0, 12);
        //when(rr.startConnection()).then(rr.isConnected()==true);

        //System.out.println();
        //rr.writeIntToRegister(2, 44);
    }

    @Test
    public void check_readIntegerData() throws Exception {
        rr.startConnection();
        Register register = new SimpleRegister(10);
        Register register2 = new SimpleRegister(14);
        Register[] registers = new Register[]{register, register2, new SimpleRegister(45)};
        when(tcpMaster.readMultipleRegisters(anyInt(), anyInt(), anyInt())).thenReturn(registers);
        List<Double> readedData = rr.readIntegerData();
        Assert.assertEquals(14.0,readedData.get(1),0.0001);
        Assert.assertEquals(45.0,readedData.get(2),0.0001);

        verify(tcpMaster,times(2)).readMultipleRegisters(anyInt(),anyInt(),anyInt());

        rr.stopConnection();
    }




    @Test
    @Ignore
    public void resetFlagTest() throws ModbusException, InterruptedException {
        rr.resetFlag(0);

    }

}
