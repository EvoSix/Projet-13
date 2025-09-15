package com.poc.POC_CHAT.repositories;

import com.poc.POC_CHAT.models.chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<chat, Long> {

    @Query(
            value = """
        SELECT c.* FROM chats c
        JOIN conversation cv ON cv.chat_id = c.id
        WHERE cv.user_id = :userId
        """,
            countQuery = """
        SELECT COUNT(*) FROM chats c
        JOIN conversation cv ON cv.chat_id = c.id
        WHERE cv.user_id = :userId
        """,
            nativeQuery = true
    )
    Page<chat> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
