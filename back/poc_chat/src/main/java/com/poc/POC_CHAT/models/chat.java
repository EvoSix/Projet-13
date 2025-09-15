package com.poc.POC_CHAT.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;


@Entity
@Table(name = "chats")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class chat {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column
private String content;

@Column
private LocalDateTime created_at;

@Column
private LocalDateTime updated_at;

}
