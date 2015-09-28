package eu.luckyApp.settings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import eu.luckyApp.websocket.FlowMeasurementHandler;
import eu.luckyApp.websocket.MeasurementHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	
//	@Value("${ws.url}")
//	String webSocketUrl;
	
	

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(measurementHandler(), "/measurementHandler");
		registry.addHandler(flowMeasurementHandler(), "/flowMeasurementHandler");
		

	}
	
	@Bean
	public WebSocketHandler measurementHandler(){
		return new MeasurementHandler();
}
	
	@Bean
	public WebSocketHandler flowMeasurementHandler(){
		return new FlowMeasurementHandler();
	}

}
