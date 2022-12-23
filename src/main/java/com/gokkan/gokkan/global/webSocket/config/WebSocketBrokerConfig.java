package com.gokkan.gokkan.global.webSocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/queue", "/topic");
		//queue = 메시지가 1대1로 송신될 때
		//topic = 메시지가 1대다로 송신될 때, subscribe
		registry.setApplicationDestinationPrefixes("/auction");
		//app = 경로로 시작하는 STOMP 메세지의 "destination" 헤더는 @Controller 객체의 @MessageMapping 메서드로 라우팅
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/gokkan")
			//WebSocket 또는 SockJS Client가 웹소켓 핸드셰이크 커넥션을 생성할 경로
			.setAllowedOrigins("*")
			.withSockJS();
	}
}
