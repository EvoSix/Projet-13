package com.poc.POC_CHAT.securities;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketInboundConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtChannelInterceptor jwtChannelInterceptor;
    private final DebugInboundInterceptor debugInboundInterceptor;
    public WebSocketInboundConfig(JwtChannelInterceptor i, DebugInboundInterceptor d) { this.jwtChannelInterceptor = i;    this.debugInboundInterceptor = d; }

    @Override
    public void configureClientInboundChannel(ChannelRegistration reg) {
        reg.interceptors(jwtChannelInterceptor);
    }
}