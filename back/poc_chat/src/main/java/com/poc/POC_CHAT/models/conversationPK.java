package com.poc.POC_CHAT.models;

import jakarta.persistence.*;

import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class conversationPK {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "chat_id")
    private Long chatId;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof conversationPK)) return false;
        conversationPK that = (conversationPK) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(chatId, that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, chatId);
    }

    @Override
    public String toString() {
        return "conversationPK{" +
                "userId=" + userId +
                ", chatId=" + chatId +
                '}';
    }

}
