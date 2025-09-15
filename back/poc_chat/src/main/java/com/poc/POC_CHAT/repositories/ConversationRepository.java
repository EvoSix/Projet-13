package com.poc.POC_CHAT.repositories;



import com.poc.POC_CHAT.models.conversation;
import com.poc.POC_CHAT.models.conversationPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<conversation, conversationPK> {
    Page<conversation> findAll(Pageable pageable);
    Page<conversation> findByUser_Id(Long userId, Pageable pageable);
    @Query("""
        SELECT cv FROM conversation cv
        JOIN FETCH cv.user
        JOIN FETCH cv.chat
        WHERE cv.id.chatId = :chatId
        """)
    Optional<conversation> findOneByChatIdWithUser(@Param("chatId") Long chatId);
}

