package com.poc.POC_CHAT.securities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private  UserDetailsService userDetailsService;

    public JwtChannelInterceptor(JwtService jwtService, UserDetailsService uds) {
        this.jwtService = jwtService;
        this.userDetailsService = uds;

    }

    @Override
    public Message<?> preSend(Message<?> msg, MessageChannel ch) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
                msg,
                StompHeaderAccessor.class
        );
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {

            var natives = accessor.toNativeHeaderMap();
            System.out.println("[JWT] CONNECT natives=" + natives);

            String auth = accessor.getFirstNativeHeader("Authorization");
            if (auth == null) auth = accessor.getFirstNativeHeader("authorization");

            if (auth == null || !auth.startsWith("Bearer ")) {
                System.out.println("[JWT] CONNECT sans Bearer -> drop");
                return null; // drop propre : évite l'exception bruyante
            }
            if (auth != null && auth.startsWith("Bearer ")) {
                String raw = auth.substring(7).trim();
                String username = jwtService.extractSubject(raw);
                if (jwtService.isTokenValid(raw, username)) {
                    UserDetails user = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    System.out.println("[JWT] AUTHENTICATION token=" + authentication);
                    accessor.setUser(authentication); //
                } else {
                    return null; // drop propre
                }
            } else {
                return null;   // drop propre si pas de Bearer
            }

    }
        return msg;
    }
}