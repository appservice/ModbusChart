package eu.luckyApp.settings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import eu.luckyApp.websocket.MeasurementHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	
//	@Value("${ws.url}")
//	String webSocketUrl;
	
	

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(measurementHandler(), "/measurementHandler");

	}
	
	@Bean
	public WebSocketHandler measurementHandler(){
		return new MeasurementHandler();
	}

}
