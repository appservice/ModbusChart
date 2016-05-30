package eu.luckyApp.rest;

import eu.luckyApp.AbstractControllerTest;
import eu.luckyApp.model.ServerEntity;
import eu.luckyApp.repository.ServerRepository;


import eu.luckyApp.settings.jersey.JerseyConfig;
import eu.luckyApp.settings.jersey.JerseyServletRegistration;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.AbstractClientHttpResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;


import javax.ws.rs.core.Application;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Lukasz Mochel
 * Private company LuckyApp
 * Created by LMochel on 2016-03-28.
 */

public class ServersServiceTest extends AbstractControllerTest {

    private final static String ENDPOINT_ADDRESS = "http://localhost:8081/rest";


    @Mock
    ServerRepository serverRepository;

    @InjectMocks
    ServersService serversService;



    @Before
    public  void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(serversService);


    }

    private ServerEntity getServerEntityStubData() {
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

/*
    protected Application configure() {
        MockitoAnnotations.initMocks(this);
        this.serverEntity = initTestingServerEntity();
        when(serverRepository.findOne(1L)).thenReturn(serverEntity);
        serversService = new ServersService();
        serversService.setServerRepository(serverRepository);


        return new ResourceConfig().register(serversService);
    }*/


    @Test
    public void getAll() throws Exception {

    }

    @Ignore
    @Test
    public void getServer() throws Exception {
        Long id = new Long(1);
        String uri = "http://localhost:8090/ModbusChart/rest/servers/";

        RestTemplate restTemplate=new RestTemplate();
/*
        String sessionCookie= restTemplate.execute(uri, HttpMethod.POST, request -> {
            request.getBody().write(("j_username=admin&j_password=admin123").getBytes());
        }, response -> {
            AbstractClientHttpResponse r = (AbstractClientHttpResponse) response;
            HttpHeaders headers = r.getHeaders();
            return headers.get("Set-Cookie").get(0);
        });*/

      //  ServerEntity serverEntity=restTemplate.getForEntity(uri,ServerEntity.class).getBody();
       ResponseEntity<ServerEntity>entity= restTemplate.getForEntity(uri,ServerEntity.class,id);


        when(serverRepository.findOne(any())).thenReturn(getServerEntityStubData());

       // MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri, id)).andReturn();
        verify(serversService, times(1)).getServer(id);

       // int status = result.getResponse().getStatus();
        int status=entity.getStatusCode().value();

        Assert.assertEquals("failure - expected HTTP STATUS 200", 200, status);
    }

    @Ignore
    @Test
    public void addServer() throws Exception {

    }

    @Ignore
    @Test
    public void updateServer() throws Exception {

    }
    @Ignore
    @Test
    public void deleteServer() throws Exception {

    }
    @Ignore
    @Test
    public void deleteServerById() throws Exception {

    }
    @Ignore
    @Test
    public void getServerExecutor() throws Exception {

    }
    @Ignore
    @Test
    public void showRegisterRs() throws Exception {

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {


    }
}