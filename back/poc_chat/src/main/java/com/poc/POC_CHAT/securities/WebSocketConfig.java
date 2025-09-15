package com.poc.POC_CHAT.securities;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry reg) {

        reg.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS().setSuppressCors(true);;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry cfg) {
        cfg.enableSimpleBroker("/topic", "/queue");
        cfg.setApplicationDestinationPrefixes("/app");
        cfg.setUserDestinationPrefix("/user");
    }
}
