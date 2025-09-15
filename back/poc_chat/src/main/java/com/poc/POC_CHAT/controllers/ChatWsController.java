package com.poc.POC_CHAT.controllers;

import com.poc.POC_CHAT.dtos.ChatCreateRequestDTO;
import com.poc.POC_CHAT.dtos.ChatDTO;
import com.poc.POC_CHAT.services.ChatService;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatWsController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messaging;

    public ChatWsController(ChatService chatService, SimpMessagingTemplate messaging) {
        this.chatService = chatService;
        this.messaging = messaging;
    }


    @MessageMapping("/chats.create")
    public void createChat(@Valid @Payload ChatCreateRequestDTO req, SimpMessageHeaderAccessor headers) {
System.out.println("Create Chat" + headers);
        Principal user = headers.getUser();
        ChatDTO saved = chatService.create( user.getName(),req);


        messaging.convertAndSend("/topic/chats", saved);


        messaging.convertAndSendToUser(user.getName(), "/queue/acks", saved);
    }


    @MessageMapping("/chats.update")
    public void updateChat(@Valid @Payload ChatUpdateRequest req, Principal principal) {
        ChatDTO updated = chatService.update(req.id(), req.toCreateRequest(), principal.getName());
        messaging.convertAndSend("/topic/chats", updated);
        messaging.convertAndSendToUser(principal.getName(), "/queue/acks", updated);
    }


    @MessageMapping("/chats.delete")
    public void deleteChat(@Payload Long id, Principal principal) {
        chatService.delete(id, principal.getName());
        messaging.convertAndSend("/topic/chats.deleted", id);
        messaging.convertAndSendToUser(principal.getName(), "/queue/acks", id);
    }


    public record ChatUpdateRequest(Long id, ChatCreateRequestDTO data) {
        ChatCreateRequestDTO toCreateRequest() { return data; }
    }
}