package eu.luckyApp.rest;

import eu.luckyApp.model.ServerEntity;
import eu.luckyApp.repository.ServerRepository;


import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;


import javax.ws.rs.core.Application;

import static org.mockito.Mockito.when;

/**
 * Lukasz Mochel
 * Private company LuckyApp
 * Created by LMochel on 2016-03-28.
 */
@RunWith(MockitoJUnitRunner.class)
public class ServersServiceTest extends JerseyTest {

    private final static String ENDPOINT_ADDRESS = "http://localhost:8081/rest";

    @InjectMocks
    ServersService serversService;
    @Mock
    ServerRepository serverRepository;

    ServerEntity serverEntity;



    public ServersServiceTest() {




    }

    @Override
    protected Application configure() {
        MockitoAnnotations.initMocks(this);
        this.serverEntity = initTestingServerEntity();
        when(serverRepository.findOne(1L)).thenReturn(serverEntity);
        serversService = new ServersService();
        serversService.setServerRepository(serverRepository);


        return new ResourceConfig().register(serversService);
    }


    private static void startServer() throws Exception {

    }


    @Before
    public void setUp() throws Exception {





    }


    private ServerEntity initTestingServerEntity() {
        ServerEntity serverEntity = new ServerEntity();
        serverEntity.setName("PLC");
        serverEntity.setId(1L);
        serverEntity.setIp("127.0.0.1");
        serverEntity.setPort(502);
        serverEntity.setFirstRegisterPos(0);
        serverEntity.setReadedDataType("INTEGER");
        serverEntity.setSavedMeasurementNumber(10);
        serverEntity.setScaleFactor(1);
        serverEntity.setScaleFactorForElectricEnergy(1);
        return serverEntity;
    }

    @Test
    public void getAll() throws Exception {
      // target(ENDPOINT_ADDRESS).request().get(ServerEntity.class);
        System.out.print(target("/test").getUri());
    }

    @Test
    public void getServer() throws Exception {

    }

    @Test
    public void addServer() throws Exception {

    }

    @Test
    public void updateServer() throws Exception {

    }

    @Test
    public void deleteServer() throws Exception {

    }

    @Test
    public void deleteServerById() throws Exception {

    }

    @Test
    public void getServerExecutor() throws Exception {

    }

    @Test
    public void showRegisterRs() throws Exception {

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {


    }
}