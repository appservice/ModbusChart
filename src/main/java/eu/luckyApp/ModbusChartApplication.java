package eu.luckyApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;

@SpringBootApplication//(exclude = {ErrorMvcAutoConfiguration.class})
@PropertySource("application.properties")
//@EnableScheduling
public class ModbusChartApplication {
	


	private static class MyCustomizer implements EmbeddedServletContainerCustomizer {

	    @Override
	    public void customize(ConfigurableEmbeddedServletContainer container) {
	        container.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/errors/401.html"));
	        container.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/errors/403.html"));
	        container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/errors/404.html"));
	        


	    }}
	
	    @Bean
	    public EmbeddedServletContainerCustomizer containerCustomizer(){
	        return new MyCustomizer();
	    }
	    
    public static void main(String[] args) {
        SpringApplication.run(ModbusChartApplication.class, args);
       
     
    }
}
