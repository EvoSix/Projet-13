package com.poc.POC_CHAT.securities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.*;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.List;

@Slf4j
@Component
public class DebugInboundInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor acc = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (acc != null && acc.getCommand() != null) {
            Map<String, List<String>> natives = acc.toNativeHeaderMap();
            log.info("[INBOUND] cmd={} dest={} session={} user={} natives={}",
                    acc.getCommand(), acc.getDestination(), acc.getSessionId(), acc.getUser(), natives);
        }
        return message;
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        if (ex != null) {
            StompHeaderAccessor acc = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            log.error("[INBOUND:EX] cmd={} dest={} session={} user={} -> {}",
                    (acc != null ? acc.getCommand() : null),
                    (acc != null ? acc.getDestination() : null),
                    (acc != null ? acc.getSessionId() : null),
                    (acc != null ? acc.getUser() : null),
                    ex.toString(), ex);
        }
    }
}