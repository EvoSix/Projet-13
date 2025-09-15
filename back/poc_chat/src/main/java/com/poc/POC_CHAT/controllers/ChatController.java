package com.poc.POC_CHAT.controllers;

import com.poc.POC_CHAT.dtos.*;
import com.poc.POC_CHAT.services.ChatService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) { this.chatService = chatService; }

    @PostMapping
    public ResponseEntity<ChatDTO> create(@Valid @RequestBody ChatCreateRequestDTO request,
                                                  Authentication auth) {
        return ResponseEntity.ok(chatService.create(auth.getName(), request));
    }

    @GetMapping
    public ResponseEntity<Page<ChatDTO>> list(
            @RequestParam(required = false) Long userId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(chatService.list(userId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(chatService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChatDTO> update(@PathVariable Long id,
                                                  @Valid @RequestBody ChatCreateRequestDTO request,
                                                  Authentication auth) {
        return ResponseEntity.ok(chatService.update(id, request, auth.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        chatService.delete(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}