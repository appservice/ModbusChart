package eu.luckyApp.settings;

import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import eu.luckyApp.settings.jersey.JerseyConfig;


@Configuration
public class JerseyServletRegistration {
	@Bean
	public ServletRegistrationBean	jerseyServlet(){
		ServletRegistrationBean registration=new ServletRegistrationBean(new ServletContainer(), "/rest/*");
		registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());
		//registration.
		return registration;
		
	}
}
