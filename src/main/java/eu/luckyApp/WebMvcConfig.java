package eu.luckyApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

//@Configuration
//@EnableWebMvc
public class WebMvcConfig extends WebMvcAutoConfigurationAdapter {
	
	@Autowired
    EmbeddedWebApplicationContext server;
	

@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
	
	//	registry.addResourceHandler("/view/**").addResourceLocations("/app/src/");//classpath:webapp
	//	System.out.println("TEST TO JEST"+"classpath:");
	//	Resource resource=server.getResource("classpath:");
	//	System.out.println(resource.getFilename());
		//System.out.println(server.getServletConfig().getServletName());
		
	}



/*
	@Bean
	public InternalResourceViewResolver viewResolver(){
		InternalResourceViewResolver resolver=new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/html/");
		resolver.setSuffix(".html");
		return resolver;
	}*/


	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
	      registry.addViewController("/errors/404").setViewName("errors/404");
	      registry.addViewController("/errors/500").setViewName("errors/500");
	      //registry.addViewController("/WEB-INF/html/login").setViewName("login");
	}
	
	

}
