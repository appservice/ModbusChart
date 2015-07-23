package eu.luckyApp;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// (exclude = {ErrorMvcAutoConfiguration.class})
// @PropertySource("application.properties")
@EnableScheduling
public class ModbusChartApplication extends SpringBootServletInitializer {

	//private static Class<ModbusChartApplication> applicationClass = ModbusChartApplication.class;

	private static class MyCustomizer implements
			EmbeddedServletContainerCustomizer {

		@Override
		public void customize(ConfigurableEmbeddedServletContainer container) {
			container.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED,
					"/errors/401.html"));
			container.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN,
					"/errors/403.html"));
			container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
					"/errors/404.html"));

		}
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new MyCustomizer();
	}

	public static void main(String[] args) {
		SpringApplication.run(ModbusChartApplication.class, args);

	}

	//VVVVVVVVVVVVVVVVVVVV skasuj gdy wildfly
	/*  @Override protected SpringApplicationBuilder configure(
	  SpringApplicationBuilder application) { return
	 application.sources(applicationClass); }
	  
	  private static Class<ModbusChartApplication>applicationClass=ModbusChartApplication.class;*/
	//^^^^^^^^^^^^^^^^^^^^^^^^



}
