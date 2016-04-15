package eu.luckyApp.modbus;

import com.rits.cloning.Cloner;
import eu.luckyApp.modbus.service.RegisterReader;
import eu.luckyApp.model.ServerEntity;
import eu.luckyApp.repository.FilePathRepository;
import eu.luckyApp.repository.ServerRepository;
import eu.luckyApp.websocket.FlowMeasurementHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.Uri;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class FlowMeasurementHandlerTest {

	FlowMeasurementHandler flowMeasurementHandler;

	private static final String ENDPOINT_ADDRESS="http://localhost:8080/rest";


	@Mock
	ServerRepository serverRepository;

	@Mock
	FilePathRepository filePathRepository;

	@Mock
	Cloner cloner;

	@Mock
	RegisterReader registerReader;


	public FlowMeasurementHandlerTest() {
		MockitoAnnotations.initMocks(this);
	}


	@BeforeClass
	public void initialize() throws Exception {
		startServer();

	}

	@Before
	public void setUp() throws Exception {
		flowMeasurementHandler=new FlowMeasurementHandler();
		ServerEntity serverEntity = createMockServer();
		when(serverRepository.findOne(Matchers.anyLong())).thenReturn(serverEntity);





		//when(serverRepository.findOne(Matchers.anyLong()))
	}

	private static void startServer() {
		URI baseUri= UriBuilder.fromUri("http://localhost").port(9998).build();
		//ResourceConfig config=new ResourceConfig()
	}

	@Test
	public void name() throws Exception {

	}


	private ServerEntity createMockServer() {
		ServerEntity serverEntity=new ServerEntity();
		serverEntity.setTimeInterval(200);
		serverEntity.setReadedDataType("INTEGER");
		serverEntity.setFirstRegisterPos(0);
		serverEntity.setScaleFactor(1);
		serverEntity.setScaleFactorForElectricEnergy(1);
		serverEntity.setSavedMeasurementNumber(10);
		List<String> sensrosName= Arrays.asList(new String[]{"pierwszy","drugi","trzeci"});
		serverEntity.setSensorsName(sensrosName);
		return serverEntity;
	}
}
