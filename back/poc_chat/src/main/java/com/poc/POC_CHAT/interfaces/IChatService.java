package com.poc.POC_CHAT.interfaces;

import com.poc.POC_CHAT.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IChatService {
    ChatDTO create(String currentUsername, ChatCreateRequestDTO request);
    Page<ChatDTO> list(Long userId, Pageable pageable); // si userId null => tout
    ChatDTO get(Long id);
    ChatDTO update(Long id, ChatCreateRequestDTO request, String currentUsername);
    void delete(Long id, String currentUsername);
}
