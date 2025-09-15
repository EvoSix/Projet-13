package com.poc.POC_CHAT.securities;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;



public class WebSocketSecurityConfig
        extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                // autoriser les frames “techniques”
                .simpTypeMatchers(
                        SimpMessageType.CONNECT,
                        SimpMessageType.HEARTBEAT,
                        SimpMessageType.UNSUBSCRIBE,
                        SimpMessageType.DISCONNECT
                ).permitAll()

                // messages applicatifs -> authentifié
                .simpDestMatchers("/app/**").authenticated()

                // souscriptions
                .simpSubscribeDestMatchers("/topic/**").authenticated()
                .simpSubscribeDestMatchers("/queue/**", "/user/**").authenticated()

                // le reste refusé (ou authenticated si tu préfères) :
                .anyMessage().denyAll();
    }

    // si tu bloque l’origine côté proxy, tu peux désactiver same-origin ici
    @Override
    protected boolean sameOriginDisabled() { return true; }


}