package com.poc.POC_CHAT.models;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "conversation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class conversation {

        @EmbeddedId
        private conversationPK id = new conversationPK();

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @MapsId("userId")
        @JoinColumn(name = "user_id")
        private user user;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @MapsId("chatId")
        @JoinColumn(name = "chat_id")
        private chat chat;


    }


