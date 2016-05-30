package eu.luckyApp;

import eu.luckyApp.settings.jersey.JerseyConfig;
import eu.luckyApp.settings.jersey.JerseyServletRegistration;
import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by luke on 30.04.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes ={ModbusChartApplication.class, JerseyConfig.class, JerseyServletRegistration.class})
public abstract class AbstractSpringTest {

  protected   Logger logger=Logger.getLogger(this.getClass());
}
