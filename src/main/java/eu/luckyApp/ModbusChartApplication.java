package eu.luckyApp;

import com.rits.cloning.Cloner;
import eu.luckyApp.modbus.facade.MyModbusTCPMaster;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.ws.rs.BeanParam;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@PropertySource(value = "classpath:application.properties")
@EnableConfigurationProperties
public class ModbusChartApplication extends SpringBootServletInitializer {

	private static Class<ModbusChartApplication> applicationClass = ModbusChartApplication.class;

	private static class MyCustomizer implements EmbeddedServletContainerCustomizer {

		@Override
		public void customize(ConfigurableEmbeddedServletContainer container) {
			container.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/login.html"));
			container.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/errors/403.html"));
			container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/errors/404.html"));

		}
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new MyCustomizer();
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/ModbusChart/");
		return viewResolver;
	}


	@Bean
	public Cloner cloner(){
		return new Cloner();
	}

	//@Bean
	//public MyModbusTCPMaster myModbusTCPMaster(){
	//	MyModbusTCPMaster mm=new MyModbusTCPMaster();
//	}


	public static void main(String[] args) {
		SpringApplication.run(ModbusChartApplication.class, args);

	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		
		return application.sources(applicationClass);
	}

	

	
}
