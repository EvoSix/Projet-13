package com.poc.POC_CHAT.services;


import com.poc.POC_CHAT.dtos.*;
import com.poc.POC_CHAT.interfaces.IChatService;
import com.poc.POC_CHAT.models.chat;
import com.poc.POC_CHAT.models.conversation;
import com.poc.POC_CHAT.models.conversationPK;
import com.poc.POC_CHAT.models.user;
import com.poc.POC_CHAT.mappers.ChatMapper;
import com.poc.POC_CHAT.repositories.ChatRepository;
import com.poc.POC_CHAT.repositories.ConversationRepository;
import com.poc.POC_CHAT.repositories.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;


@Service
@Transactional
public class ChatService implements IChatService {

    private final ChatRepository chats;
    private final ConversationRepository convs;
    private final UserRepository users;


    public ChatService(ChatRepository chats, ConversationRepository convs,
                           UserRepository users) {
        this.chats = chats; this.convs = convs; this.users = users;
    }

    @Override
    public ChatDTO create(String currentUsername, ChatCreateRequestDTO request) {
        user u = users.findByEmail(currentUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur introuvable"));

        chat entity = new chat();
        entity.setContent(request.content());
        entity.setCreated_at(LocalDateTime.now());
        entity.setUpdated_at(LocalDateTime.now());
        chat saved = chats.save(entity);

        // crée la ligne de jointure user<->chat
        conversation link = new conversation();
        link.setId(new conversationPK(u.getId(), saved.getId()));
        link.setUser(u);
        link.setChat(saved);

        convs.save(link);

        return toDto(link);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatDTO> list(Long userId, Pageable pageable) {
        Page<conversation> page = (userId == null)
                ? convs.findAll(pageable)
                : convs.findByUser_Id(userId, pageable);

        return page.map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ChatDTO get(Long id) {
        conversation cv = convs.findOneByChatIdWithUser(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message introuvable"));
        return toDto(cv);
    }

    @Override
    public ChatDTO update(Long id, ChatCreateRequestDTO request, String currentUsername) {
        chat c = chats.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message introuvable"));
        c.setContent(request.content());
        c.setUpdated_at(LocalDateTime.now());
        chats.save(c);

        return get(id);
    }

    @Override
    public void delete(Long id, String currentUsername) {
        if (!chats.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message introuvable");
        }
        chats.deleteById(id);
    }


    private ChatDTO toDto(conversation cv) {
        var u = cv.getUser();
        var c = cv.getChat();
        return new ChatDTO(
                c.getId(),
                c.getContent(),
                u.getFirstname(),
                u.getLastname(),
                c.getCreated_at(),
                c.getUpdated_at()
        );
    }
}
